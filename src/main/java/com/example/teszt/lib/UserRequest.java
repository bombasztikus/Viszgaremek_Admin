package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class UserRequest {

    public final String full_name;
    public final String email;
    public final Boolean is_employee;


    public UserRequest(String full_name, String email, Boolean is_employee) {
        this.full_name = full_name;
        this.email = email;
        this.is_employee = is_employee;
    }

    public HashMap<String, Object> toJSON(){
        HashMap<String, Object> json = new HashMap<>();

        json.put("full_name", full_name);
        json.put("email", email);
        json.put("is_employee", is_employee);

        return json;
    }

    public User useredit(int id) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(toJSON());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/users/" + id))
                    .header("Content-Type", "application/json")
                    .header("Authorization","Bearer " +  Authentication.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
            User valasz = User.from_json(responseMap);

            if (valasz != null && response.statusCode() == 200) {
                return valasz;
            }

            return valasz;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
