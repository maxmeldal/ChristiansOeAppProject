package com.example.christiansoeappproject.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.Base64;


public class Attraction extends Location implements Parcelable {

    @SerializedName("video")
    private String video;

    @SerializedName("audio")
    private String audio;

    @SerializedName("image")
    private String image;

    @SerializedName("description")
    private String description;

    public Attraction(String id, double latitude, double longitude, String name, String video, String audio, String image, String description) {
        super(id, latitude, longitude, name);
        this.video = video;
        this.audio = audio;
        this.image = image;
        this.description = description;
    }

    public Attraction(double latitude, double longitude, String name, String video, String audio, String image, String description) {
        super(latitude, longitude, name);
        this.video = video;
        this.audio = audio;
        this.image = image;
        this.description = description;
    }

    /**
     * Constructor bliver brugt til at oprette Attraction med video audio og image, ud fra byte arrays
     * Setter bliver kaldt i stedet for constructor
     */
    public Attraction(String id, double latitude, double longitude, String name, String description) {
        super(id, latitude, longitude, name);
        this.description = description;
    }

    public Attraction(double latitude, double longitude, String name, String description) {
        super(latitude, longitude, name);
        this.description = description;
    }

    protected Attraction(Parcel in) {
        super(in.readString(), in.readDouble(), in.readDouble(), in.readString());
        video = in.readString();
        audio = in.readString();
        image = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getId());
        dest.writeDouble(this.getLatitude());
        dest.writeDouble(this.getLongitude());
        dest.writeString(this.getName());
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(image);
        dest.writeString(description);
    }

    public static final Creator<Attraction> CREATOR = new Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel in) {
            return new Attraction(in);
        }

        @Override
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] getVideo() {
        if (video != null) return Base64.getDecoder().decode(video.getBytes());
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setVideo(byte[] video) {
        if (video!=null) this.video = Base64.getEncoder().encodeToString(video);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] getAudio() {
        if (audio != null) return Base64.getDecoder().decode(audio.getBytes());
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAudio(byte[] audio) {
        if (audio!=null) this.audio = Base64.getEncoder().encodeToString(audio);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] getImage(){
        if (image!=null) return Base64.getDecoder().decode(image.getBytes());
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setImage(byte[] image) {
        if (image!=null) this.image = Base64.getEncoder().encodeToString(image);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString() + "Attraction{" +
                "description='" + description + '\'' +
                '}';
    }
}
