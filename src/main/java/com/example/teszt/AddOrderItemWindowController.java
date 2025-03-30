package com.example.teszt;

import com.example.teszt.lib.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddOrderItemWindowController implements Initializable {

    @FXML
    private ChoiceBox<Meal> mealSelection;

    @FXML
    private TextField quantityField;

    private HelloController mainController;

    private static Order selectedOrder;

    private static List<Meal> meals;

    public static void setSelectedOrder(Order selectedOrder) {
        AddOrderItemWindowController.selectedOrder = selectedOrder;
    }

    public static void setMeals(List<Meal> meals) {
        AddOrderItemWindowController.meals = meals;
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void handleOrderItemAddition(ActionEvent event) {
        int quantity = Integer.parseInt(quantityField.getText());
        try {
            OrderItemRequest.create(quantity, mealSelection.getSelectionModel().getSelectedItem().getId(), selectedOrder.getId());
            mainController.updateorderitems();
        } catch (Api_error e) {
            showAPIExceptionModal(e.error);
        }

        Stage stage = (Stage) mealSelection.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mealSelection.getItems().addAll(meals);
    }

    private void showAPIExceptionModal(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("A tétel nem adható hozzá");
        alert.setHeaderText("Kérlek javítsd ki a hibát, majd próbáld újra.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
