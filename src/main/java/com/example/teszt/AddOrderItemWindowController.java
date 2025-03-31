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
import java.util.Objects;
import java.util.ResourceBundle;

public class AddOrderItemWindowController implements Initializable {

    @FXML
    private ChoiceBox<Meal> mealSelection;

    @FXML
    private Label mealSelectionRequiredLabel;

    @FXML
    private TextField quantityField;

    @FXML
    private Label quantityRequiredLabel;

    @FXML
    private Button edit_orderitem;

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
            OrderItemRequest.create(quantity, mealSelection.getSelectionModel().getSelectedItem().id, selectedOrder.id);
            mainController.updateorderitems();
        } catch (Api_error e) {
            showApiExceptionPopUp(e.error);
        }

        Stage stage = (Stage) mealSelection.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mealSelection.getItems().addAll(meals);
        fieldValueManager();
        buttonDisableManager();
        requiredFieldManager();
    }

    private void fieldValueManager() {
        mealSelection.setItems(FXCollections.observableArrayList(meals));

        mealSelection.setConverter(new StringConverter<Meal>() {
            @Override
            public String toString(Meal meal) {
                return meal == null ? "" : meal.toString();
            }

            @Override
            public Meal fromString(String s) {
                for (Meal m : meals) {
                    if (Objects.equals(m.toString(), s)) {
                        return m;
                    }
                }

                return null;
            }
        });

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
        edit_orderitem.disableProperty().bind(Bindings.or(
                quantityField.textProperty().isEmpty(),
                mealSelection.valueProperty().isNull()
        ));
    }

    private void requiredFieldManager() {
        quantityRequiredLabel.managedProperty().bind(quantityRequiredLabel.visibleProperty());
        quantityRequiredLabel.visibleProperty().bind(quantityField.textProperty().isEmpty());

        mealSelectionRequiredLabel.managedProperty().bind(mealSelectionRequiredLabel.visibleProperty());
        mealSelectionRequiredLabel.visibleProperty().bind(mealSelection.valueProperty().isNull());

        List<TextField> fields = new ArrayList<>();
        fields.add(quantityField);

        for (TextField field : fields) {
            invalidFieldClassManager(field, field.getText());

            field.textProperty().addListener((observable, oldValue, newValue) -> {
                invalidFieldClassManager(field, newValue);
            });
        }

        invalidChoiceClassManager(mealSelection, mealSelection.getValue());
        mealSelection.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
            invalidChoiceClassManager(mealSelection, newValue.getSelectedItem());
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
        alert.setHeaderText("Probléma adódott a tétel hozzáadása közben");
        alert.showAndWait();
    }
}
