package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class OrderRequest {

    public final String address;
    public final Boolean is_completed;

    public OrderRequest(String address, Boolean is_completed) {
        this.address = address;
        this.is_completed = is_completed;
    }

    public HashMap<String, Object> toJSON(){
        HashMap<String, Object> json = new HashMap<>();

        json.put("address", address);
        json.put("is_completed", is_completed);


        return json;
    }

    public Order orderedit(int id) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(toJSON());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/orders/" + id))
                    .header("Content-Type", "application/json")
                    .header("Authorization","Bearer " +  Authentication.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
            Order valasz = Order.from_json(responseMap);

            if (valasz != null && !valasz.get_is_error()) {
                return valasz;
            }

            return valasz;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
