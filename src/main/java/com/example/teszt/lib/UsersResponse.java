package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UsersResponse {
    public List<User> items;
    public Boolean is_error;

    public UsersResponse(List<User> items, Boolean is_error) {
        this.items = items;
        this.is_error = is_error;
    }
    static public UsersResponse from_json(HashMap json) {
        if ((Boolean) json.get("is_error")) {
            return null;
        }
        List<HashMap> items_raw= (List<HashMap>) json.get("items");
        //HashMap<String, Object>[] items_raw = (HashMap<String, Object>[]) json.get("items");
        List<User> items = new ArrayList<>();
        for (int i = 0; i < items_raw.size(); i++) {
            items.add(User.from_json(items_raw.get(i)));
        }
        return new UsersResponse(
                items,
                (Boolean) json.get("is_error")
        );
    }
    public String toString() {
        return "UsersResponse(" + " items_count: " + this.items.size() + ")";
    }
}
