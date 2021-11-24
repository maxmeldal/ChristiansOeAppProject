package com.example.christiansoeappproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class Trip {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("info")
    private String info;

    @SerializedName("theme")
    private int theme;

    private List<Attraction> attractions;

    public Trip(String id, String name, String info, int theme, List<Attraction> attractions)
    {
        this.id = id;
        this.name = name;
        this.info = info;
        this.theme = theme;
        this.attractions = attractions;
    }
    public Trip(String name, String info, int theme, List<Attraction> attractions)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.info = info;
        this.theme = theme;
        this.attractions = attractions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }
}
