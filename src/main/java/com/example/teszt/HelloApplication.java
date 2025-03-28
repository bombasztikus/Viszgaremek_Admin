package com.example.teszt;

import com.example.teszt.lib.Api;
import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Authentication;
import com.example.teszt.lib.LoginRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
        showCustomPopUp();

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

    private void showCustomPopUp() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);

        TextField email = new TextField();
        PasswordField password = new PasswordField();

        Api backend = Api.getApi();

        TextField api = new TextField(backend.getApiBase());
        Button b = new Button("Login");

        email.setMaxWidth(150);
        password.setMaxWidth(150);
        api.setMaxWidth(150);

        password.setPromptText("Password");
        email.setPromptText("Username");

        Text l1 = new Text("Email:");
        Text p1 = new Text("Jelszó:");
        Text a = new Text("API link: ");

        VBox popupLayout = new VBox(10);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(l1, email, p1, password, a, api, b);

        b.setOnAction(e -> {
            LoginRequest adatok = new LoginRequest(email.getText(), password.getText() );
            backend.setApiBase(api.getText());
            try {
                Authentication.signIn(adatok);
            } catch (Api_error ex) {
                showLoginError(ex.error);

            }

            if (Authentication.isLoggedIn()) {
                if (Authentication.getIsAdmin()) {
                    popupStage.close();
                } else {
                    showLoginError("Nem vagy Admin");
                    Authentication.signOut();
                }
            }
        });

        email.setOnAction(e -> b.fire());
        password.setOnAction(e -> b.fire());

        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Bejelentkezés");

        popupStage.showAndWait();
    }

    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Bejelentkezési Hiba");
        alert.setHeaderText("Bejelentkezés nem sikerült");
        alert.setContentText(message);
        alert.showAndWait();
    }
}