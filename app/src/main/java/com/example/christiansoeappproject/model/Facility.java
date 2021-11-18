package com.example.christiansoeappproject.model;

public class Facility extends Location{
    public Facility(String id, double latitiude, double longitude, String name) {
            super(id, latitiude, longitude, name);
    }

    public Facility(double latitude, double longitude, String name){
            super(latitude, longitude, name);
    }
}
