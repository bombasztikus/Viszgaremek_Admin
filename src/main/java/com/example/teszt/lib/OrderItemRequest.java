package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class OrderItemRequest {

    public final String quantity;

    public OrderItemRequest(String quantity) {
        this.quantity = quantity;
    }

    public HashMap<String, Object> toJSON(){
        HashMap<String, Object> json = new HashMap<>();

        json.put("quantity", quantity);

        return json;
    }

    public Orderitem orderitemedit(int order_id, int meal_id) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(toJSON());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/orders/" + order_id + "/items/" + meal_id))
                    .header("Content-Type", "application/json")
                    .header("Authorization","Bearer " +  Authentication.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);

            if (responseMap.containsKey("is_error") && (Boolean) responseMap.get("is_error")) {
                throw Api_error.from_json(responseMap);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
