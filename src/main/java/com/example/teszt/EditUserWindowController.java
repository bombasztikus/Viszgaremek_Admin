package com.example.teszt;

import com.example.teszt.lib.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditUserWindowController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private Label nameRequiredLabel;

    @FXML
    private CheckBox workercheck;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailRequiredLabel;

    @FXML
    private Button add_meal;

    private HelloController mainController;

    private static User selectedUser;

    public static void setSelectedUser(User selectedUser) {
        EditUserWindowController.selectedUser = selectedUser;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleEditMeal() {
        String full_name = nameField.getText();
        String email = emailField.getText();
        Boolean worker = getSelectedUser().id == 1 ? true : workercheck.isSelected();

        UserRequest request = new UserRequest(full_name, email, worker);
        try {
            request.useredit(selectedUser.id);
            mainController.updateusers();
        } catch (Api_error e) {
            System.out.println(e);
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (selectedUser != null) {
            nameField.setText(selectedUser.getName());
            emailField.setText(selectedUser.getEmail());
            workercheck.setSelected(selectedUser.is_employee);
            workercheck.setDisable(selectedUser.id == 1);
        }

        buttonDisableManager();
        requiredFieldManager();
    }

    private void buttonDisableManager() {
        add_meal.disableProperty().bind(Bindings.or(
                nameField.textProperty().isEmpty(),
                emailField.textProperty().isEmpty()
        ));
    }

    private void requiredFieldManager() {
        nameRequiredLabel.managedProperty().bind(nameRequiredLabel.visibleProperty());
        nameRequiredLabel.visibleProperty().bind(nameField.textProperty().isEmpty());

        emailRequiredLabel.managedProperty().bind(emailRequiredLabel.visibleProperty());
        emailRequiredLabel.visibleProperty().bind(emailField.textProperty().isEmpty());

        List<TextField> fields = new ArrayList<>();
        fields.add(nameField);
        fields.add(emailField);

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
        alert.setTitle("Probléma adódott");
        alert.setContentText(message);
        alert.setHeaderText("Probléma adódott a felhasználó szerkesztése közben");
        alert.showAndWait();
    }
}