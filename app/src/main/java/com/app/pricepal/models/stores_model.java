package com.app.pricepal.models;

public class stores_model {
    private int id;
    private String storeName;
    private String storeAddress;
    private double storeGeoLocationLang;
    private double storeGeoLocationLat;
    private String storeImg;
    private boolean storeStatus;
    private double price;

    public stores_model(int id, String storeName,String storeAddress, Double storeGeoLocationLang,
                        Double storeGeoLocationLat,String storeImg,boolean storeStatus) {
        this.id = id;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeGeoLocationLang = storeGeoLocationLang;
        this.storeGeoLocationLat=storeGeoLocationLat;
        this.storeImg=storeImg;
        this.storeStatus=storeStatus;
    }


    public stores_model(int id, String storeName, String storeAddress, Double storeGeoLocationLang,
                        Double storeGeoLocationLat, String storeImg, double price, boolean storeStatus) {
        this.id = id;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeGeoLocationLang = storeGeoLocationLang;
        this.storeGeoLocationLat=storeGeoLocationLat;
        this.storeImg=storeImg;
        this.price=price;
        this.storeStatus=storeStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public double getStoreGeoLocationLang() {
        return storeGeoLocationLang;
    }

    public void setStoreGeoLocationLang(double storeGeoLocationLang) {
        this.storeGeoLocationLang = storeGeoLocationLang;
    }

    public double getStoreGeoLocationLat() {
        return storeGeoLocationLat;
    }

    public void setStoreGeoLocationLat(double storeGeoLocationLat) {
        this.storeGeoLocationLat = storeGeoLocationLat;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public boolean isStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(boolean storeStatus) {
        this.storeStatus = storeStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return storeName;
    }

}

