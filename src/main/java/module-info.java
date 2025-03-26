module com.example.teszt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens com.example.teszt.lib to javafx.base;
    exports com.example.teszt;
    opens com.example.teszt to javafx.base, javafx.fxml;
}