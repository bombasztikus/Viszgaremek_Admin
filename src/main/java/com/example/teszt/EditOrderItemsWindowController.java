package com.example.teszt;

import com.example.teszt.lib.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditOrderItemsWindowController implements Initializable {
    @FXML
    private Button edit_order;

    @FXML
    private TextField quantityField;

    private HelloController mainController;

    private static Orderitem selectedOrderItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quantityField.setText(String.valueOf(selectedOrderItems.quantity));

    }

    public static void setSelectedOrder(Orderitem selectedOrder) {
        EditOrderItemsWindowController.selectedOrderItems = selectedOrder;
    }

    public static Orderitem getSelectedOrderItems() {
        return selectedOrderItems;
    }

    @FXML
    void handleEditOrderItems(ActionEvent event) {
        String quantity = quantityField.getText();

        OrderItemRequest request = new OrderItemRequest(quantity);
        try {
            Orderitem editOrder = request.orderitemedit(selectedOrderItems.order_id, selectedOrderItems.meal_id);
            mainController.updateorderitems();
        } catch (Api_error e) {
            showLoginError(e.error);
        }

        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rendelési probléma");
        alert.setHeaderText("Rendelés gond");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
