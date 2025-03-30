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

public class User implements ObservableList {
    public Integer id;
    private String email;
    private String full_name;
    public Boolean is_employee;

    public User(Integer id, String email, String full_name, Boolean is_employee) {
        this.id = id;
        this.email = email;
        this.full_name = full_name;
        this.is_employee = is_employee;
    }

    static public User from_json(HashMap json) {
        return new User(
                (Integer) json.get("id"),
                (String) json.get("email"),
                (String) json.get("full_name"),
                (Boolean) json.get("is_employee")
        );
    }

    @Override
    public String toString() {
        return "User(id: " + this.id + " name: " + this.full_name + ")";
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getIs_employee() {
        return is_employee ? "Alkalmazott" : "Ügyfél";
    }

    public String getId() {
        return "# " + id;
    }

    public static LinkedHashMap<String, String> getTableColums() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();

        columns.put("id", "Azonosító");
        columns.put("full_name", "Teljes Név");
        columns.put("email", "Email Cím");
        columns.put("is_employee", "Szerepkör");

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

    public String getName() {return full_name;
    }

    public static void delete(User user) throws Api_error {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Api.getApi().getApiBase() + "/users/" + user.getId()))
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

