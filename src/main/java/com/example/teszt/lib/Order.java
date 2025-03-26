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

public class Order extends Meal implements ObservableList {
    private Integer id;
    private Integer user_id;
    private String date_created;
    private String address;
    private Boolean is_completed;

    public Order(Integer id, Integer user_id, String date_created, String address, Boolean is_completed) {
        this.id = id;
        this.user_id = user_id;
        this.date_created = date_created;
        this.address = address;
        this.is_completed = is_completed;
    }

    static public Order from_json(HashMap json) throws Api_error {
        if ((Boolean) json.get("is_error")) {
            throw Api_error.from_json(json);
        }

        return new Order(
                (Integer) json.get("id"),
                (Integer) json.get("user_id"),
                (String) json.get("date_created"),
                (String) json.get("address"),
                (Boolean) json.get("is_completed")
        );
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public static HashMap<String, String> getTableColums() {
        HashMap<String, String> columns = new HashMap<>();

        columns.put("id", "Azonosító");
        columns.put("user_id", "Felhasználó");
        columns.put("date_created", "Leadva");
        columns.put("address", "Átvételi Hely");
        columns.put("is_completed", "Kész");

        return columns;
    }

    @Override
    public void addListener(ListChangeListener listChangeListener) {}

    @Override
    public void removeListener(ListChangeListener listChangeListener) {}

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
    public void remove(int i, int i1) {}

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
    public void clear() {}

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {}

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
    public void addListener(InvalidationListener invalidationListener) {}

    @Override
    public void removeListener(InvalidationListener invalidationListener) {}

    public static void delete(Order order) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase() + "/orders/" + order.getId()))
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