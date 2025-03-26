package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class MealRequest {

    public final String name;
    public final Integer price;
    public final Integer calories;
    public final String image_url;
    public final String description;
    public final MealType type;

    public MealRequest(String name, Integer price, Integer calories, String image_url, String description, MealType type) {
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.image_url = image_url;
        this.description = description;
        this.type = type;
    }

    public HashMap<String, Object> toJSON(){
        HashMap<String, Object> json = new HashMap<>();

        json.put("name", name);
        json.put("price", price);
        json.put("calories", calories);
        json.put("image_url", image_url);
        json.put("description", description);

        return json;
    }

    public Meal mealadd() throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(toJSON());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase()+"/meals/" + type.toString()))
                    .header("Content-Type", "application/json")
                    .header("Authorization","Bearer " +  Authentication.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
            Meal valasz = Meal.from_json(responseMap);

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
