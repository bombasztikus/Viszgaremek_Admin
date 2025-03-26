package com.example.teszt.lib;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.HashMap;

public class LoginRequest {
    public final String email;
    public final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public HashMap<String, String> toJSON(){
        HashMap<String, String> json = new HashMap<>();

        json.put("email", email);
        json.put("password", password);
        return json;
    }
}
