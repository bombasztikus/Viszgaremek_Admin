package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class Orderitem {
    private Integer meal_id;
    private Integer order_id;
    public Integer quantity;
    private Meal meal;

    public Orderitem(Integer meal_id, Integer order_id, Integer quantity, Meal meal) {
        this.meal_id = meal_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.meal = meal;
    }


    public static void delete(Orderitem orderitem) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase() + "/orders/" + orderitem.order_id + "/items/" + orderitem.meal_id))
                    .header("Authorization", "Bearer " + Authentication.getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204) {
                ObjectMapper mapper = new ObjectMapper();
                HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
                if (responseMap.containsKey("is_error")) {
                    throw Api_error.from_json(responseMap);
                }
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getMeal_id() {
        return "#" + meal_id;
    }

    public String getOrder_id() {
        return "#" + order_id;
    }

    public String getQuantity() {
        return quantity + " db.";
    }

    public Meal getMeal() {
        return meal;
    }

    public String getMeal_name() {
        return meal.getName();
    }

    public String getMeal_price() {
        return meal.getPrice() + " Ft";
    }

    public String getTotal_price() {
        return meal.getPrice() * quantity + " Ft";
    }

    static public Orderitem from_json(HashMap json) throws Api_error {

        return new Orderitem(
                (Integer) json.get("meal_id"),
                (Integer) json.get("order_id"),
                (Integer) json.get("quantity"),
                Meal.from_json((HashMap) json.get("meal"))
        );
    }

    public static HashMap<String, Integer> toeditJSON(int quantity) {
        HashMap<String, Integer> json = new HashMap<>();

        json.put("quantity", quantity);

        return json;
    }

    public static HashMap<String, String> getTableColums() {
        HashMap<String, String> columns = new HashMap<>();

        columns.put("meal_id", "Termék azonosító");
        columns.put("meal_name", "Terméknév");
        columns.put("quantity", "Darabszám");
        columns.put("meal_price", "Darabár");
        columns.put("total_price", "Összesen fizetendő");

        return columns;
    }

    public boolean get_is_error() {
        return is_error();
    }

    private boolean is_error() {
        return is_error();
    }
}
