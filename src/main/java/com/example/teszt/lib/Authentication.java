package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private static Authentication instance;
    private  LoginResponse loginResponse;
    //private ErrorRespon

    private Authentication(LoginResponse response) {
        loginResponse = response;
    }

    public static LoginResponse signIn(LoginRequest credentials) throws Api_error {
        if (instance != null) {
            return instance.loginResponse;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(credentials.toJSON());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
            LoginResponse valasz = LoginResponse.from_json(responseMap);

            if (valasz != null && !valasz.is_error) {
                Authentication.instance = new Authentication(valasz);
            }

            return valasz;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getToken() {
        return Authentication.instance.loginResponse.getAccess_token();
    }

    public static boolean isLoggedIn() {
        return Authentication.instance != null;
    }

    public static boolean getIsAdmin() {
        try {
            // Step 1: Split the JWT token into 3 parts (header, payload, and signature)
            String[] parts = Authentication.instance.loginResponse.getAccess_token().split("\\.");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            // Step 2: Base64 decode the payload (second part of the JWT)
            String payload = parts[1];

            // Base64 URL decode (replace URL-safe characters with standard Base64 characters)
            String decodedPayload = new String(Base64.getDecoder().decode(payload.replace('-', '+').replace('_', '/')));

            // Step 3: Use Jackson to parse the decoded payload into a Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map jsonPayload = objectMapper.readValue(decodedPayload, Map.class);

            // Step 4: Extract the role claim from the Map
            String role = (String) jsonPayload.get("role");  // Adjust "role" if your JWT has a different claim name

            // Output the role
            return role.equalsIgnoreCase("admin");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error decoding JWT: " + e.getMessage());
        }

        return false;
    }
    public static void signOut() {
        Authentication.instance = null;
    }
}
