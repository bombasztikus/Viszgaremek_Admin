package com.example.teszt;

import com.example.teszt.lib.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TableView<Meal> Menu_table;

    @FXML
    private TableView<User> User_table;

    @FXML
    private TableView<Order> Orders_table;

    @FXML
    private Button add_button;

    @FXML
    private MenuBar menu;

    @FXML
    private TextField search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Meal> meals = fetch_meals();
            load_meals(meals);
        } catch (Api_error e) {

        }

        List<User> users = fetch_users();
        load_users(users);

        List<Order> orders = fetch_orders();
        load_orders(orders);

        add_button.setOnAction(event -> openAddMealWindow());

    }

    private void openAddMealWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/AddMealWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.AddMealWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New Meal");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedMeal() {
        Meal selectedMeal = Menu_table.getSelectionModel().getSelectedItem();
        if (selectedMeal != null) {
            try {
                Meal.delete(selectedMeal);
                Menu_table.getItems().remove(selectedMeal);
            } catch (Api_error e) {
                System.out.println(e);
            }
        }
    }

    public void addMealToTable(Meal meal) {
        Menu_table.getItems().add(meal);
    }

    private List<Meal> fetch_meals() throws Api_error {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/meals"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);

            MealsResponse valasz = MealsResponse.from_json(responseMap);

            if (valasz != null) {
                return valasz.items;
            } else {
                System.err.println("Failed to parse meals response.");
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void load_meals(List<Meal> meals) {
        for (String key : Meal.getTableColums().keySet()) {
            TableColumn<Meal, ?> column = new TableColumn<>(Meal.getTableColums().get(key));
            column.setCellValueFactory(new PropertyValueFactory<>(key));
            Menu_table.getColumns().add(column);
        }

        for (Meal meal : meals) {
            Menu_table.getItems().add(meal);
        }
    }

    private List<User> fetch_users() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/users"))
                    .setHeader("Authorization", "Bearer " + Authentication.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);

            UsersResponse valasz = UsersResponse.from_json(responseMap);

            if (valasz != null && !valasz.is_error) {
                return valasz.items;
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void load_users(List<User> users) {
        for (String key : User.getTableColums().keySet()) {
            TableColumn<User, ?> column = new TableColumn<>(User.getTableColums().get(key));
            column.setCellValueFactory(new PropertyValueFactory<>(key));
            User_table.getColumns().add(column);
        }

        for (User user : users) {
            User_table.getItems().add(user);
        }
    }

    private List<Order> fetch_orders() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/orders"))
                    .setHeader("Authorization", "Bearer " + Authentication.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);

            OrdersResponse valasz = OrdersResponse.from_json(responseMap);

            if (valasz != null && !valasz.is_error) {
                return valasz.items;
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void load_orders(List<Order> orders) {
        for (String key : Order.getTableColums().keySet()) {
            TableColumn<Order, ?> column = new TableColumn<>(Order.getTableColums().get(key));
            column.setCellValueFactory(new PropertyValueFactory<>(key));
            Orders_table.getColumns().add(column);
        }

        for (Order order : orders) {
            Orders_table.getItems().add(order);
        }
    }
}