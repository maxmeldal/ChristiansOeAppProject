package com.example.christiansoeappproject.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.Base64;


public class Attraction extends Location {

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
        if (audio==null && video==null && image==null){
            return super.toString() + "Attraction{" +
                    "video='null" + '\'' +
                    ", audio='null" + '\'' +
                    ", image='null" + '\'' +
                    '}';
        }
        if (audio == null && image==null){
            return super.toString() + "Attraction{" +
                    "video='" + video.substring(0, 10) + '\'' +
                    ", audio='null" + '\'' +
                    ", image='null" + '\'' +
                    '}';
        }
        if (video==null && image==null){
            return super.toString() + "Attraction{" +
                    "video='null" + '\'' +
                    ", audio='" + audio.substring(0, 10) + '\'' +
                    ", image='null" + '\'' +
                    '}';
        }
        if (audio==null && video==null){
            return super.toString() + "Attraction{" +
                    "video='null" + '\'' +
                    ", audio='null" + '\'' +
                    ", image='" + image.substring(0,10) +  '\'' +
                    '}';
        }
        return super.toString() + "Attraction{" +
                "video='" + video.substring(0, 10) + '\'' +
                ", audio='" + audio.substring(0, 10) + '\'' +
                '}';
    }
}
