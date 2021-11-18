package com.example.christiansoeappproject.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant extends Location{

    @SerializedName("url")
    private String url;

    @SerializedName("open")
    private double open;

    @SerializedName("close")
    private double close;

    public Restaurant(String id, double latitiude, double longtitude, String name, String url, double open, double close){
        super(id, latitiude, longtitude, name);
        this.url = url;
        this.open = open;
        this.close = close;
    }

    public Restaurant(double latitude, double longitude, String name, String url, double open, double close) {
        super(latitude, longitude, name);
        this.url = url;
        this.open = open;
        this.close = close;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
