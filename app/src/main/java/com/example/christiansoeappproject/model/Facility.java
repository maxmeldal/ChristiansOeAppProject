package com.example.christiansoeappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Facility extends Location implements Parcelable{
    public Facility(String id, double latitiude, double longitude, String name) {
            super(id, latitiude, longitude, name);
    }

    public Facility(double latitude, double longitude, String name){
            super(latitude, longitude, name);
    }

    protected Facility(Parcel in) {
        super(in.readString(), in.readDouble(), in.readDouble(), in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeDouble(this.getLatitude());
        parcel.writeDouble(this.getLongitude());
        parcel.writeString(this.getName());
    }

    public static final Creator<Facility> CREATOR = new Creator<Facility>() {
        @Override
        public Facility createFromParcel(Parcel in) {
            return new Facility(in);
        }

        @Override
        public Facility[] newArray(int size) {
            return new Facility[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
