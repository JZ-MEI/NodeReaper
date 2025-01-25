package com.nebula.nodereaper.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import java.io.File;

public class ReaperController {
    @FXML
    private TextField path;
    @FXML
    private Button chooseBtn;
    @FXML
    private Label tipsText;
    @FXML
    private Button removeBtn;
    @FXML
    private Label progressText;

    @FXML
    private void onChooseButtonClick() {
        // 创建一个 DirectoryChooser 实例
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // 设置对话框标题
        directoryChooser.setTitle("选择一个文件夹");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory==null){
            return;
        }
        path.setText(selectedDirectory.getAbsolutePath());
    }

    @FXML
    public void onRemoveButtonClick() {
        if (StrUtil.isEmpty(path.getText())) {
            tipsText.setText("你似乎还没有选择项目");
            return;
        }

        tipsText.setText("让我们来找找那个大家伙");
        File file = new File(path.getText());

        if (!FileUtil.exist(file)) {
            tipsText.setText("看起来你的路径似乎不存在");
            return;
        }

        if (!FileUtil.isDirectory(file)) {
            tipsText.setText("你选的不是一个文件夹");
            return;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            tipsText.setText("看起来它是一个空文件夹而不是你的项目");
            return;
        }

        // 使用 Task 执行后台任务
        Task<Void> deleteTask = new Task<>() {
            private long startTime;

            @Override
            protected Void call() {
                startTime = System.currentTimeMillis(); // 记录任务开始时间
                for (File currentFile : files) {
                    if (StrUtil.equals(currentFile.getName(), "node_modules")) {
                        updateMessage("Java 正在优雅地从您的电脑中移除 node_modules...");
                        recursiveDelete(currentFile);
                        FileUtil.del(currentFile);
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                long endTime = System.currentTimeMillis(); // 记录任务结束时间
                long duration = endTime - startTime; // 计算耗时
                String formattedTime = String.format("%.2f", duration / 1000.0); // 转换为秒，保留两位小数
                tipsText.textProperty().unbind();
                tipsText.setText("Java 已经优雅地从您的电脑中移除 node_modules，Java和Node一次完美的合作");
                progressText.setText("耗时: " + formattedTime + " 秒");
            }

            @Override
            protected void failed() {
                super.failed();
                tipsText.textProperty().unbind();
                tipsText.setText("删除操作失败，请检查路径或权限");
            }
        };

        // 绑定任务的消息到 tipsText
        tipsText.textProperty().bind(deleteTask.messageProperty());

        // 启动任务
        Thread taskThread = new Thread(deleteTask);
        taskThread.setDaemon(true); // 守护线程
        taskThread.start();
    }

    /**
     * 递归删除文件夹内容，并更新进度
     */
    private void recursiveDelete(File file) {
        Platform.runLater(() -> progressText.setText(file.getAbsolutePath()));
        if (!FileUtil.isDirectory(file)) {
            FileUtil.del(file);
            return;
        }
        File[] innerFiles = file.listFiles();
        if (innerFiles != null) {
            for (File currentFile : innerFiles) {
                recursiveDelete(currentFile);
            }
        }
        FileUtil.del(file);
    }
}