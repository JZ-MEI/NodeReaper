module com.nebula.nodereaper {
    requires javafx.controls;
    requires javafx.fxml;
    requires cn.hutool;


    opens com.nebula.nodereaper to javafx.fxml;
    opens com.nebula.nodereaper.controller to javafx.fxml;
    exports com.nebula.nodereaper;
}