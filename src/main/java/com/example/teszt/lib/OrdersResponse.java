package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersResponse {
    public List<Order> items;
    public Boolean is_error;

    public OrdersResponse(List<Order> items, Boolean is_error) {
        this.items = items;
        this.is_error = is_error;
    }

    static public OrdersResponse from_json(HashMap<String, Object> json) throws Api_error {
        List<HashMap<String, Object>> items_raw = (List<HashMap<String, Object>>) json.get("items");
        List<Order> items = new ArrayList<>();
        if (items_raw != null) {
            for (HashMap<String, Object> item_raw : items_raw) {
                items.add(Order.from_json(item_raw));
            }
        }
        return new OrdersResponse(
                items,
                (Boolean) json.getOrDefault("is_error", false)
        );
    }

    @Override
    public String toString() {
        return "OrdersResponse(items_count: " + this.items.size() + ")";
    }
}