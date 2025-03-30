package com.example.teszt;

import com.example.teszt.lib.Api;
import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Authentication;
import com.example.teszt.lib.LoginRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        showLoginWindow();

        if (Authentication.isLoggedIn() && Authentication.getIsAdmin()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1050, 700);
            stage.setTitle("Admin");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Nem sikerült bejelentkezni így az alkalmazás nem indult el!");
            System.exit(0);
        }
    }

    private void showLoginWindow() {
        try {
             FXMLLoader loginWindowLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/teszt/LoginWindow.fxml"));
             Parent root = loginWindowLoader.load();

             Stage stage = new Stage();
             stage.setResizable(false);
             stage.initModality(Modality.WINDOW_MODAL);
             stage.setTitle("Bejelentkezés");
             stage.setScene(new Scene(root));
             stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Bejelentkezési Hiba");
        alert.setHeaderText("Bejelentkezés nem sikerült");
        alert.setContentText(message);
        alert.showAndWait();
    }
}