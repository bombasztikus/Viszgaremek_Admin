package com.example.teszt;

import com.example.teszt.lib.Api;
import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Authentication;
import com.example.teszt.lib.LoginRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.beans.binding.Bindings;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable {

    @FXML
    private TextField backendField;

    @FXML
    private Label backendRequiredLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailRequiredLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordRequiredLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        Api backend = Api.getApi();
        backend.setApiBase(backendField.getText());

        LoginRequest credentials = new LoginRequest(emailField.getText(), passwordField.getText());

        try {
            Authentication.signIn(credentials);
        } catch (Api_error e) {
            showApiExceptionPopUp(e.error);
        }

        if (Authentication.isLoggedIn()) {
            if (Authentication.getIsAdmin()) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            } else {
                showApiExceptionPopUp("Az alkalmazás használatához jelentkezz be egy alkalmazotti fiókkal.");
                Authentication.signOut();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonDisableManager();
        requiredFieldManager();

        backendField.setText(Api.getApi().getApiBase());
    }

    private void buttonDisableManager() {
        loginButton.disableProperty().bind(Bindings.or(
            Bindings.or(
                    emailField.textProperty().isEmpty(),
                    passwordField.textProperty().isEmpty()
            ),
            backendField.textProperty().isEmpty()
        ));
    }

    private void requiredFieldManager() {
        backendRequiredLabel.managedProperty().bind(backendRequiredLabel.visibleProperty());
        backendRequiredLabel.visibleProperty().bind(backendField.textProperty().isEmpty());

        emailRequiredLabel.managedProperty().bind(emailRequiredLabel.visibleProperty());
        emailRequiredLabel.visibleProperty().bind(emailField.textProperty().isEmpty());

        passwordRequiredLabel.managedProperty().bind(passwordRequiredLabel.visibleProperty());
        passwordRequiredLabel.visibleProperty().bind(passwordField.textProperty().isEmpty());

        List<TextField> fields = new ArrayList<>();
        fields.add(backendField);
        fields.add(emailField);
        fields.add(passwordField);

        for (TextField field : fields) {
            invalidFieldClassManager(field, field.getText());

            field.textProperty().addListener((observable, oldValue, newValue) -> {
                invalidFieldClassManager(field, newValue);
            });
        }
    }

    private void invalidFieldClassManager(TextField field, String value) {
        if (value != null && value.isEmpty()) {
            if (!field.getStyleClass().contains("invalid")) {
                field.getStyleClass().add("invalid");
            }
        } else {
            field.getStyleClass().remove("invalid");
        }

        field.applyCss();
        field.layout();
    }

    private void showApiExceptionPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sikertelen bejelentkezés");
        alert.setContentText(message);
        alert.setHeaderText("Sikertelen bejelentkezés");
        alert.showAndWait();
    }
}
