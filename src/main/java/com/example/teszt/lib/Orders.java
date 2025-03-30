package com.example.teszt.lib;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Orders implements ObservableList {
    private Integer id;
    private Integer user_id;
    private String data_created;
    private Boolean address;
    private String is_completed;

    public Orders(Integer id, Integer user_id, String data_created, Boolean address, String is_completed) {
        this.id = id;
        this.user_id = user_id;
        this.data_created = data_created;
        this.address = address;
        this.is_completed = is_completed;
    }

    static public Orders from_json(HashMap json) {
        return new Orders(
                (Integer) json.get("id"),
                (Integer) json.get("user_id"),
                (String) json.get("data_created"),
                (Boolean) json.get("address"),
                (String) json.get("is_completed")
        );
    }

    public String getId() {
        return "# " + id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getData_created() {
        return data_created;
    }

    public Boolean getAddress() {
        return address;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public static LinkedHashMap<String, String> getTableColums() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();

        columns.put("id", "Integer");
        columns.put("user_id", "Integer");
        columns.put("data_created", "String");
        columns.put("address", "String");
        columns.put("is_completed", "String");

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
}