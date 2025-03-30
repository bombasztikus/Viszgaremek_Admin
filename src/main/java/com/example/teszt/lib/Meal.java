package com.example.teszt.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Meal implements ObservableList {
    private boolean is_error;
    private String name;
    private MealType type;
    private Integer calories;
    private String description;
    public Integer id;
    private String image_url;
    private Integer price;
    private Integer stars;
    private Boolean has_image_url;
    private String fallback_image_url;

    public boolean get_is_error() {
        return is_error;
    }

    public Meal(String name, MealType type, Integer calories, String description, Integer id, String image_url, Integer price, Integer stars, Boolean has_image_url, String fallback_image_url, Boolean is_error) {
        this.name = name;
        this.type = type;
        this.calories = calories;
        this.description = description;
        this.id = id;
        this.image_url = image_url;
        this.price = price;
        this.stars = stars;
        this.has_image_url = has_image_url;
        this.fallback_image_url = fallback_image_url;
        this.is_error = is_error;
    }

    public Meal() {
    }

    public Boolean is_free() {
        return this.price == 0;
    }

    static public Meal from_json(HashMap json) throws Api_error {
        if ((Boolean) json.get("is_error")) {
            throw Api_error.from_json(json);
        }
        return new Meal(
                (String) json.get("name"),
                string_to_mealtype((String) json.get("type")),
                (Integer) json.get("calories"),
                (String) json.get("description"),
                (Integer) json.get("id"),
                (String) json.get("image_url"),
                (Integer) json.get("price"),
                (Integer) json.get("stars"),
                (boolean) json.get("has_image_url"),
                (String) json.get("fallback_image_url"),
                (boolean) json.get("is_error")

        );
    }

    public static MealType string_to_mealtype(String value) {
        return switch (value.toLowerCase()) {
            case "food" -> MealType.FOOD;
            case "menu" -> MealType.MENU;
            case "beverage" -> MealType.BEVERAGE;
            case "dessert" -> MealType.DESSERT;
            default -> throw new IllegalStateException("Váratlan MealType: " + value);
        };
    }

    @Override
    public String toString() {
        return "(" + this.getDisplay_price() + "/db.) " + this.name;
    }

    public String getName() {
        return name;
    }

    public MealType getType() {
        return type;
    }

        public String getCalories() {
        return calories + " kcal";
    }

    public String getDescription() {
        return description;
    }

    public String getDisplay_price() {
        return price + " Ft";
    }

    public String getId() {
        return "# " + id;
    }

    public String getImage_url() {
        return image_url;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStars() {
        return stars;
    }

    public static LinkedHashMap<String, String> getTableColums() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();

        columns.put("id", "Azonosító");
        columns.put("type", "Típus");
        columns.put("name", "Név");
        columns.put("display_price", "Ár");
        columns.put("calories", "Kalória");

        return columns;
    }

    @Override
    public void addListener(ListChangeListener listChangeListener) {

    }

    @Override
    public void removeListener(ListChangeListener listChangeListener) {

    }

    @Override
    public boolean addAll(Object[] objects) {
        return false;
    }

    @Override
    public boolean setAll(Object[] objects) {
        return false;
    }

    @Override
    public boolean setAll(Collection collection) {
        return false;
    }

    @Override
    public boolean removeAll(Object[] objects) {
        return false;
    }

    @Override
    public boolean retainAll(Object[] objects) {
        return false;
    }

    @Override
    public void remove(int i, int i1) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return List.of();
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {

    }

    public static void delete(Meal meal) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase() + "/meals/" + meal.getId()))
                    .header("Authorization", "Bearer " + Authentication.getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204) {
                ObjectMapper mapper = new ObjectMapper();
                HashMap responseMap = mapper.readValue(response.body(), HashMap.class);
                if (responseMap.containsKey("is_error")) {
                    throw Api_error.from_json(responseMap);
                }
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}


