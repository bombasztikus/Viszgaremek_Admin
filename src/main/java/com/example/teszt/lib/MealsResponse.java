package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MealsResponse {
    public List<Meal> items;
    public Boolean is_error;

    public MealsResponse(List<Meal> items, Boolean is_error) {
        this.items = items;
        this.is_error = is_error;
    }
    static public MealsResponse from_json(HashMap json) throws Api_error {
        List<HashMap> items_raw= (List<HashMap>) json.get("items");
        //HashMap<String, Object>[] items_raw = (HashMap<String, Object>[]) json.get("items");
        List<Meal> items = new ArrayList<>();
        for (int i = 0; i < items_raw.size(); i++) {
            items.add(Meal.from_json(items_raw.get(i)));
        }
        return new MealsResponse(
                items,
                (Boolean) json.get("is_error")
        );
    }
    public String toString() {
        return "MealsResponse(items_count: " + this.items.size() + ")";
    }
}
