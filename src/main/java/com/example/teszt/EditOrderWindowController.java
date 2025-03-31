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

public class EditOrderWindowController  implements Initializable {
    @FXML
    private Button edit_order;

    @FXML
    private TextField addressField;

    @FXML
    private Label addressRequiredLabel;

    @FXML
    private CheckBox completecheck;

    private HelloController mainController;

    private static Order selectedOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addressField.setText(selectedOrder.getAddress());
        completecheck.setSelected(selectedOrder.is_completed);

        buttonDisableManager();
        requiredFieldManager();
    }

    public static void setSelectedOrder(Order selectedOrder) {
        EditOrderWindowController.selectedOrder = selectedOrder;
    }

    public static Order getSelectedOrder() {
        return selectedOrder;
    }

    @FXML
    void handleEditMeal(ActionEvent event) {
        OrderRequest request = new OrderRequest(
                addressField.getText(),
                completecheck.isSelected()
        );

        try {
            request.orderedit(getSelectedOrder().id);
            mainController.updateorders();
        } catch (Api_error e) {
            showApiExceptionPopUp(e.error);
        }

        Stage stage = (Stage) addressField.getScene().getWindow();
        stage.close();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    private void buttonDisableManager() {
        edit_order.disableProperty().bind(addressField.textProperty().isEmpty());
    }

    private void requiredFieldManager() {
        addressRequiredLabel.managedProperty().bind(addressRequiredLabel.visibleProperty());
        addressRequiredLabel.visibleProperty().bind(addressField.textProperty().isEmpty());

        List<TextField> fields = new ArrayList<>();
        fields.add(addressField);

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
        alert.setHeaderText("Probléma adódott a rendelés módosítása közben");
        alert.showAndWait();
    }
}
