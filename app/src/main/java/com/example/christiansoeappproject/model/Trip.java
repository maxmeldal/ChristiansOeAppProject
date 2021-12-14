package com.example.christiansoeappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class Trip implements Parcelable {

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

    protected Trip(Parcel in) {
        id = in.readString();
        name = in.readString();
        info = in.readString();
        theme = in.readInt();
        attractions = in.createTypedArrayList(Attraction.CREATOR);
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

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

    public String themeToString(int i){
        if(i == 1){
            return "Nature";
        } else if(i == 2){
            return "History";
        } else if(i == 3){
            return "War";
        }
        return "Other";
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

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", theme=" + theme +
                ", attractions=" + attractions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(info);
        dest.writeInt(theme);
        dest.writeTypedList(attractions);
    }
}
