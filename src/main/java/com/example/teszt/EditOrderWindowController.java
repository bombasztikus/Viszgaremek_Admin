package com.example.teszt;

import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Order;
import com.example.teszt.lib.OrderRequest;
import com.example.teszt.lib.User;
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

public class EditOrderWindowController  implements Initializable {
    @FXML
    private Button edit_order;

    @FXML
    private TextField addressField;

    @FXML
    private CheckBox completecheck;

    private HelloController mainController;

    private static Order selectedOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addressField.setText(selectedOrder.getAddress());
        completecheck.setSelected(selectedOrder.is_completed);
    }

    public static void setSelectedOrder(Order selectedOrder) {
        EditOrderWindowController.selectedOrder = selectedOrder;
    }

    public static Order getSelectedOrder() {
        return selectedOrder;
    }

    @FXML
    void handleEditMeal(ActionEvent event) {
        String address = addressField.getText();
        boolean completed = completecheck.isSelected();

        OrderRequest request = new OrderRequest(address, completed);
        try {
            Order editOrder = request.orderedit(getSelectedOrder().id);
            mainController.updateorders();
        } catch (Api_error e) {
            showLoginError(e.error);
        }

        Stage stage = (Stage) addressField.getScene().getWindow();
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
