package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class Api {
    private static Api instance;
    private String apiBase = "https://api.vizsgaremek.halaszbendeguz.hu";

    private Api() {}

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }

    public String getApiBase() {
        return apiBase;
    }

    public static Api getApi() {
        if (instance == null) {
            Api.instance = new Api();
        }

        return instance;
    }
}
