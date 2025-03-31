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
    private ChoiceBox<String> status;

    @FXML
    private Label statusRequiredLabel;

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
            showApiExceptionPopUp(e.error);
        }

        Stage stage = (Stage) addressField.getScene().getWindow();
        stage.close();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    private void fieldValueManager() {
        s.setItems(FXCollections.observableArrayList(MealType.values())); // Ensure MealType is used, not Strings

        typeField.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealType mealType) {
                if (mealType == null) return "";
                return switch (mealType) {
                    case FOOD -> "Étel";
                    case BEVERAGE -> "Ital";
                    case MENU -> "Menü";
                    case DESSERT -> "Desszert";
                };
            }

            @Override
            public MealType fromString(String string) {
                return switch (string) {
                    case "Étel" -> MealType.FOOD;
                    case "Ital" -> MealType.BEVERAGE;
                    case "Menü" -> MealType.MENU;
                    case "Desszert" -> MealType.DESSERT;
                    default -> null;
                };
            }
        });
    }

    private void buttonDisableManager() {
        add_meal.disableProperty().bind(Bindings.or(
                Bindings.or(
                        nameField.textProperty().isEmpty(),
                        typeField.valueProperty().isNull()
                ),
                Bindings.or(
                        caloriesField.textProperty().isEmpty(),
                        priceField.textProperty().isEmpty()
                )
        ));
    }

    private void requiredFieldManager() {
        nameRequiredLabel.managedProperty().bind(nameRequiredLabel.visibleProperty());
        nameRequiredLabel.visibleProperty().bind(nameField.textProperty().isEmpty());

        typeRequiredLabel.managedProperty().bind(typeRequiredLabel.visibleProperty());
        typeRequiredLabel.visibleProperty().bind(typeField.valueProperty().isNull());

        caloriesRequiredLabel.managedProperty().bind(caloriesRequiredLabel.visibleProperty());
        caloriesRequiredLabel.visibleProperty().bind(caloriesField.textProperty().isEmpty());

        priceRequiredLabel.managedProperty().bind(priceRequiredLabel.visibleProperty());
        priceRequiredLabel.visibleProperty().bind(priceField.textProperty().isEmpty());

        List<TextField> fields = new ArrayList<>();
        fields.add(nameField);
        fields.add(caloriesField);
        fields.add(priceField);

        for (TextField field : fields) {
            invalidFieldClassManager(field, field.getText());

            field.textProperty().addListener((observable, oldValue, newValue) -> {
                invalidFieldClassManager(field, newValue);
            });
        }

        invalidChoiceClassManager(typeField, typeField.getValue());
        typeField.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
            invalidChoiceClassManager(typeField, newValue.getSelectedItem());
        });
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

    private <T> void invalidChoiceClassManager(ChoiceBox<T> choices, T value) {
        if (value != null) {
            if (!choices.getStyleClass().contains("invalid")) {
                choices.getStyleClass().add("invalid");
            }
        } else {
            choices.getStyleClass().remove("invalid");
        }

        choices.applyCss();
        choices.layout();
    }

    private void showApiExceptionPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Probléma adódott");
        alert.setContentText(message);
        alert.setHeaderText("Probléma adódott a termék hozzáadása közben");
        alert.showAndWait();
    }
}
