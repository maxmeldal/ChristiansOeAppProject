package com.example.christiansoeappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Restaurant extends Location implements Parcelable {

    @SerializedName("url")
    private String url;

    @SerializedName("open")
    private double open;

    @SerializedName("close")
    private double close;

    @SerializedName("description")
    private String description;

    public Restaurant(String id, double latitiude, double longtitude, String name, String url, double open, double close, String description){
        super(id, latitiude, longtitude, name);
        this.url = url;
        this.open = open;
        this.close = close;
        this.description = description;
    }

    public Restaurant(double latitude, double longitude, String name, String url, double open, double close, String description) {
        super(latitude, longitude, name);
        this.url = url;
        this.open = open;
        this.close = close;
        this.description = description;
    }

    protected Restaurant(Parcel in) {
        super(in.readString(), in.readDouble(), in.readDouble(), in.readString());
        url = in.readString();
        open = in.readDouble();
        close = in.readDouble();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeDouble(this.getLatitude());
        parcel.writeDouble(this.getLongitude());
        parcel.writeString(this.getName());
        parcel.writeString(this.url);
        parcel.writeDouble(this.open);
        parcel.writeDouble(this.close);
        parcel.writeString(this.description);
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
