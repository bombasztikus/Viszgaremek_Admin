package com.example.teszt;

import com.example.teszt.lib.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserWindowController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private CheckBox workercheck;

    @FXML
    private TextField emailField;

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
        Boolean worker = getSelectedUser().getId() == 1 ? true : workercheck.isSelected();

        //if (!name.isEmpty() && )
        UserRequest request = new UserRequest(full_name, email, worker);
        try {
            User newUser = request.useredit(selectedUser.getId());
            mainController.updateusers();
            System.out.println(newUser);
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
            workercheck.setSelected(selectedUser.getIs_employee());
            workercheck.setDisable(selectedUser.getId() == 1);
        }
    }
}