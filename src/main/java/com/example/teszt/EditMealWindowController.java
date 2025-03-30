package com.example.teszt;

import com.example.teszt.lib.Api_error;
import com.example.teszt.lib.Meal;
import com.example.teszt.lib.MealRequest;
import com.example.teszt.lib.MealType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class EditMealWindowController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<MealType> typeField;

    @FXML
    private TextField caloriesField;

    @FXML
    private Button add_meal;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField imageField;

    private HelloController mainController;

    private static Meal selectedMeal;

    public static void setSelectedMeal(Meal selectedMeal) {
        EditMealWindowController.selectedMeal = selectedMeal;
    }

    public static Meal getSelectedMeal() {
        return selectedMeal;
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleEditMeal() {
        String name = nameField.getText();
        MealType type = typeField.getValue();
        Integer calories = Integer.parseInt(caloriesField.getText());
        String description = descriptionField.getText();
        Integer price = Integer.parseInt(priceField.getText());
        String image_url = imageField.getText();

        //if (!name.isEmpty() && )
        MealRequest request = new MealRequest(name, price, calories,image_url, description, type);
        try {
            Meal newMeal = request.mealedit(getSelectedMeal().id);
            mainController.updatemeals();
        } catch (Api_error e) {
            showLoginError(e.error);
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (selectedMeal != null) {
            nameField.setText(selectedMeal.getName());
            typeField.setValue(selectedMeal.getType());
            caloriesField.setText(String.valueOf(selectedMeal.calories));
            priceField.setText(String.valueOf(selectedMeal.getPrice()));
            imageField.setText(selectedMeal.getImage_url());
            descriptionField.setText(selectedMeal.getDescription());
        }

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
                    default -> null; // This case is unlikely to be used
                };
            }
        });
    }

    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Zaba probléma");
        alert.setHeaderText("Zaba gond");
        alert.setContentText(message);
        alert.showAndWait();
    }
}