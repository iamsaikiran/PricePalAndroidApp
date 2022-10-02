package com.app.pricepal.models;

import java.io.Serializable;

public class items_model implements Serializable {
    private String id;
    private String itemName;
    private String itemQty;
    private double itemPrice;
    private String itemImg;
    private int storeId;
    private String storeName;
    private double offerPrice;
    private boolean itemStatus;

    public items_model(String id, String itemName, String itemQty, Double itemPrice,
                       String itemImg, int storeId, String storeName, boolean itemStatus) {
        this.id = id;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
        this.itemImg=itemImg;
        this.storeId=storeId;
        this.storeName = storeName;
        this.offerPrice=offerPrice;
        this.itemStatus=itemStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQty() {
        return itemQty;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public boolean isItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public String toString() {
        return itemName;
    }

}