package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserResponse {
    public List<User> items;
    public String type_display_name;
    public MealType type;
    public Boolean is_error;

    public UserResponse(List<User> items, String type_display_name, MealType type, Boolean is_error) {
        this.items = items;
        this.type_display_name = type_display_name;
        this.type = type;
        this.is_error = is_error;
    }
    static public UserResponse from_json(HashMap json) {
        List<HashMap> items_raw= (List<HashMap>) json.get("items");
        //HashMap<String, Object>[] items_raw = (HashMap<String, Object>[]) json.get("items");
        List<User> items = new ArrayList<>();
        for (int i = 0; i < items_raw.size(); i++) {
            items.add(User.from_json(items_raw.get(i)));
        }
        return new UserResponse(
                items,
                (String) json.get("type_display_name"),
                Meal.string_to_mealtype((String) json.get("type")),
                (Boolean) json.get("is_error")
        );
    }
    public String toString() {
        return "MealsResponse(type: " + this.type + " items_count: " + this.items.size() + ")";
    }
}
