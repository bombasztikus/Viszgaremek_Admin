package com.example.teszt.lib;

import java.util.HashMap;

public class Orderitem {
    private Integer meal_id;
    private Integer order_id;
    private Integer quantity;
    private Meal meal;

    public Orderitem(Integer meal_id, Integer order_id, Integer quantity, Meal meal) {
        this.meal_id = meal_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.meal = meal;
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

    public static HashMap<String, String> getTableColums() {
        HashMap<String, String> columns = new HashMap<>();

        columns.put("meal_id", "Termék azonosító");
        columns.put("meal_name", "Terméknév");
        columns.put("quantity", "Darabszám");
        columns.put("meal_price", "Darabár");
        columns.put("total_price", "Összesen fizetendő");

        return columns;
    }
}
