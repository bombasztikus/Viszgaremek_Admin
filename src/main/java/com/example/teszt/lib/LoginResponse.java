package com.example.teszt.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginResponse {

    private final String access_token;
    private final Integer expiry;
    public  final Boolean is_error;

    public LoginResponse(String access_token, Integer expiry, Boolean is_error) {
        this.access_token = access_token;
        this.expiry = expiry;
        this.is_error = is_error;
    }

    public String getAccess_token() {
        return access_token;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public Boolean getIs_error() {
        return is_error;
    }

    static public LoginResponse from_json(HashMap json) throws Api_error {
        if ((Boolean) json.get("is_error")) {
            throw Api_error.from_json(json);
        }

        return new LoginResponse(
                (String) json.get("access_token"),
                (Integer) json.get("expiry"),
                (Boolean) json.get("is_error")
        );
    }
}
