package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderItemsResponse {
    public List<Orderitem> items;
    public String date_created;
    public Integer id;
    public boolean is_completed;
    public Boolean is_error;

    public OrderItemsResponse(List<Orderitem> items, String date_created, Integer id, boolean is_completed, Boolean is_error) {
        this.items = items;
        this.date_created = date_created;
        this.id = id;
        this.is_completed = is_completed;
        this.is_error = is_error;
    }

    static public OrderItemsResponse from_json(HashMap<String, Object> json) throws Api_error {
        List<HashMap<String, Object>> items_raw = (List<HashMap<String, Object>>) json.get("items");
        List<Orderitem> items = new ArrayList<>();
        if (items_raw != null) {
            for (HashMap<String, Object> item_raw : items_raw) {
                items.add(Orderitem.from_json(item_raw));
            }
        }
        return new OrderItemsResponse(
                items,
                (String) json.get("date_created"),
                (Integer) json.get("id"),
                (Boolean) json.get("is_completed"),
                (Boolean) json.getOrDefault("is_error", false)
        );
    }

    @Override
    public String toString() {
        return "OrderItemsResponse(items_count: " + this.items.size() + ")";
    }
}