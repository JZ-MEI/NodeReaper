package com.nebula.nodereaper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class ReaperApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ReaperApplication.class.getResource("fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        URL cssFileUrl = getClass().getResource("/com/nebula/nodereaper/css/main-view.css");
        if (cssFileUrl != null) {
            scene.getStylesheets().add(cssFileUrl.toExternalForm());
        }
        stage.setTitle("Node Reaper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}