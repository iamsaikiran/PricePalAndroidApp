package com.app.pricepal.models;

public class history_items {
    private String id;
    private String itemName;

    public history_items(String id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return itemName;
    }

}

