package com.example.teszt;

import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Meal;
import com.example.teszt.lib.MealRequest;
import com.example.teszt.lib.MealType;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMealWindowController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private Label nameRequiredLabel;

    @FXML
    private ChoiceBox<MealType> typeField;

    @FXML
    private Label typeRequiredLabel;

    @FXML
    private TextField caloriesField;

    @FXML
    private Label caloriesRequiredLabel;

    @FXML
    private Button add_meal;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private Label priceRequiredLabel;

    @FXML
    private TextField imageField;

    private HelloController mainController;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAddMeal() {
        String name = nameField.getText();
        MealType type = typeField.getValue();
        Integer calories = Integer.parseInt(caloriesField.getText());
        String description = descriptionField.getText();
        Integer price = Integer.parseInt(priceField.getText());
        String image_url = imageField.getText();

        //if (!name.isEmpty() && )
        MealRequest request = new MealRequest(name, price, calories,image_url, description, type);
        try {
            Meal newMeal = request.mealadd();
            mainController.updatemeals();
        } catch (Api_error e) {
            showApiExceptionPopUp(e.error);
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeField.getSelectionModel().select(0);
        fieldValueManager();
        buttonDisableManager();
        requiredFieldManager();
    }

    private void fieldValueManager() {
        typeField.setItems(FXCollections.observableArrayList(MealType.values())); // Ensure MealType is used, not Strings

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

        priceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    priceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        caloriesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    caloriesField.setText(newValue.replaceAll("[^\\d]", ""));
                }
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