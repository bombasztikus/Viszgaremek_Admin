package com.example.teszt;

import com.example.teszt.lib.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditOrderItemsWindowController implements Initializable {
    @FXML
    private Button edit_order;

    @FXML
    private TextField quantityField;

    @FXML
    private Label quantityRequiredLabel;

    private HelloController mainController;

    private static Orderitem selectedOrderItems;

    public static void setSelectedOrderItem(Orderitem selectedOrder) {
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
            showApiExceptionPopUp(e.error);
        }

        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quantityField.setText(String.valueOf(selectedOrderItems.quantity));
        fieldValueManager();
        buttonDisableManager();
        requiredFieldManager();
    }

    private void fieldValueManager() {
        quantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    quantityField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void buttonDisableManager() {
        edit_order.disableProperty().bind(quantityField.textProperty().isEmpty());
    }

    private void requiredFieldManager() {
        quantityRequiredLabel.managedProperty().bind(quantityRequiredLabel.visibleProperty());
        quantityRequiredLabel.visibleProperty().bind(quantityField.textProperty().isEmpty());

        List<TextField> fields = new ArrayList<>();
        fields.add(quantityField);

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
        alert.setHeaderText("Probléma adódott a tétel szerkesztése közben");
        alert.showAndWait();
    }
}
