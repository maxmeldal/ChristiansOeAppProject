package com.example.christiansoeappproject.model;

public class Attraction extends Location {
    public Attraction(String id, double latitude, double longitude, String name) {
        super(id, latitude, longitude, name);
    }

    public Attraction(double latitude, double longitude, String name) {
        super(latitude, longitude, name);
    }
}
