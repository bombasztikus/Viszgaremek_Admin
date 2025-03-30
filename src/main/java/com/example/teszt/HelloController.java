package com.example.teszt;

import com.example.teszt.lib.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
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
    private TableView<Orderitem> Orderitem_table;

    @FXML
    private Button add_button;

    @FXML
    private Button order_edit;

    @FXML
    private Button edit_button;

    @FXML
    private Button orderitem_edit;

    @FXML
    private Button user_edit;

    @FXML
    private Button delete_meal;

    @FXML
    private Button order_delete;

    @FXML
    private Button orderitem_delete;

    @FXML
    private Button orderitem_add;

    @FXML
    private Button user_delete;

    List<Orderitem> selected_orderitems = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatemeals();

        updateusers();

        updateorders();

        add_button.setOnAction(event -> openAddMealWindow());
        edit_button.setOnAction(event -> openeditMealWindow());
        user_edit.setOnAction(event -> openeditUserWindow());
        order_edit.setOnAction(event -> openEditOrderWindow());
        orderitem_edit.setOnAction(event -> openEditOrderitemWindow());

        Orders_table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Order>() {
            @Override
            public void onChanged(Change<? extends Order> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        updateorderitems();
                    }
                }
            }
        });

        configureTable(Menu_table);
        configureTable(User_table);
        configureTable(Orders_table);
        configureTable(Orderitem_table);

        delete_meal.disableProperty().bind(Menu_table.selectionModelProperty().get().selectedItemProperty().isNull());
        edit_button.disableProperty().bind(Menu_table.selectionModelProperty().get().selectedItemProperty().isNull());

        order_edit.disableProperty().bind(Orders_table.selectionModelProperty().get().selectedItemProperty().isNull());
        order_delete.disableProperty().bind(Orders_table.selectionModelProperty().get().selectedItemProperty().isNull());

        orderitem_edit.disableProperty().bind(Bindings.or(
                Orders_table.selectionModelProperty().get().selectedItemProperty().isNull(),
                Orderitem_table.selectionModelProperty().get().selectedItemProperty().isNull()
        ));
        orderitem_delete.disableProperty().bind(Bindings.or(
                Orders_table.selectionModelProperty().get().selectedItemProperty().isNull(),
                Orderitem_table.selectionModelProperty().get().selectedItemProperty().isNull()
        ));
        orderitem_add.disableProperty().bind(Orders_table.selectionModelProperty().get().selectedItemProperty().isNull());

        user_edit.disableProperty().bind(User_table.selectionModelProperty().get().selectedItemProperty().isNull());
        user_delete.disableProperty().bind(User_table.selectionModelProperty().get().selectedItemProperty().isNull());
    }

    private void configureTable(TableView<?> table) {
        table.getColumns().forEach(column -> column.setReorderable(false));
        table.setSortPolicy(x -> false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void updatemeals(){
        try {
            Menu_table.getColumns().clear();
            Menu_table.getItems().clear();
            List<Meal> meals = fetch_meals();
            load_meals(meals);
        } catch (Api_error e) {
            showLoginError(e.error);
        }
    }
    @FXML
    public void updateusers(){
        User_table.getColumns().clear();
        User_table.getItems().clear();
        List<User> users = fetch_users();
        load_users(users);
    }

    @FXML
    public void updateorders(){
        try {
            Orders_table.getColumns().clear();
            Orders_table.getItems().clear();
            Orderitem_table.getColumns().clear();
            Orderitem_table.getItems().clear();
            List<Order> orders = fetch_orders();
            load_orders(orders);
        } catch (Api_error e) {
            showLoginError(e.error);
        }
    }

    public void updateorderitems() {
        try {
            if (Orders_table.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            selected_orderitems = fetch_order_items(Orders_table.getSelectionModel().getSelectedItem().id);

            if (Orderitem_table != null) {
                Orderitem_table.getColumns().clear();
                Orderitem_table.getItems().clear();
                load_order_items(selected_orderitems);
            }
        } catch (Api_error e) {
            showLoginError(e.error);
        }
    }

    private void openAddMealWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/AddMealWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.AddMealWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Adj hozzá új kaját");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openeditMealWindow() {
        Meal selectedMeal = Menu_table.getSelectionModel().getSelectedItem();
        if (selectedMeal == null) {
            return;
        }

        EditMealWindowController.setSelectedMeal(selectedMeal);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/EditMealWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.EditMealWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit " + selectedMeal.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openeditUserWindow() {
        User selectedUser = User_table.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            return;
        }

        EditUserWindowController.setSelectedUser(selectedUser);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/EditUserWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.EditUserWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit " + selectedUser.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditOrderWindow() {
        Order selectedOrder = Orders_table.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            return;
        }
        EditOrderWindowController.setSelectedOrder(selectedOrder);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/EditOrderWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.EditOrderWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit Order" + selectedOrder.getId());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditOrderitemWindow() {
        Orderitem selectedOrder = Orderitem_table.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            return;
        }
        EditOrderItemsWindowController.setSelectedOrder(selectedOrder);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/EditOrderItemsWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.EditOrderItemsWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit Order" + selectedOrder.getQuantity());
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
                updatemeals();
            } catch (Api_error e) {
                showLoginError(e.error);
            }
        }
    }

    @FXML
    private void deleteSelectedUser() {
        User selectedUser = User_table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                User.delete(selectedUser);
                updateusers();
            } catch (Api_error e) {
                showLoginError(e.error);
            }
        }
    }

    @FXML
    private void deleteSelectedOrder() {
        Order selectedOrder = Orders_table.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                Order.delete(selectedOrder);
                updateorders();
                updateorderitems();
            } catch (Api_error e) {
                showLoginError(e.error);
            }
        }
    }

    @FXML
    private void deleteSelectedOrderItem() {
        Orderitem selectedOrder = Orderitem_table.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                Orderitem.delete(selectedOrder);
                updateorderitems();
            } catch (Api_error e) {
                showLoginError(e.error);
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

    private List<Order> fetch_orders() throws Api_error {
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

    private List<Orderitem> fetch_order_items(int order_id) throws Api_error {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/orders/" + order_id + "/items"))
                    .setHeader("Authorization", "Bearer " + Authentication.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);

            OrderItemResponse valasz = OrderItemResponse.from_json(responseMap);

            if (valasz != null && !valasz.is_error) {
                return valasz.items;
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void load_order_items(List<Orderitem> order_items) {
        for (String key : Orderitem.getTableColums().keySet()) {
            TableColumn<Orderitem, ?> column = new TableColumn<>(Orderitem.getTableColums().get(key));
            column.setCellValueFactory(new PropertyValueFactory<>(key));
            Orderitem_table.getColumns().add(column);
        }

        for (Orderitem order_item : order_items) {
            Orderitem_table.getItems().add(order_item);
        }
    }

    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Probléme de login");
        alert.setHeaderText("Probleme");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void aboutpage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ez a projekt a 2025-i vizsgaremek aw BKSZC Pogány Frigyes Technikumnak, " +
                "Készítette: Halász Willhem Bendegúz, Kőszegi Tamás Attila, Simon Attila Tibor " +
                "Github directory: https://github.com/bombasztikus/Vizsgaremek", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Rólunk");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    void addOrderItem(ActionEvent event) {
        Order selectedOrder = Orders_table.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            return;
        }

        try {
            AddOrderItemWindowController.setSelectedOrder(selectedOrder);
            AddOrderItemWindowController.setMeals(fetch_meals());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teszt/AddOrderItemWindow.fxml"));
            Parent root = loader.load();

            com.example.teszt.AddOrderItemWindowController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Add item");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Api_error e) {
            showLoginError(e.error);
        }
    }
}