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

    public Attraction(String id, double latitude, double longitude, String name, String video, String audio) {
        super(id, latitude, longitude, name);
        this.video = video;
        this.audio = audio;
    }

    public Attraction(double latitude, double longitude, String name, String video, String audio) {
        super(latitude, longitude, name);
        this.video = video;
        this.audio = audio;
    }


    public Attraction(String id, double latitude, double longitude, String name) {
        super(id, latitude, longitude, name);
    }

    public Attraction(double latitude, double longitude, String name) {
        super(latitude, longitude, name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] getVideo() {
        if (video != null) {
            return Base64.getDecoder().decode(video.getBytes());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setVideo(byte[] video) {
        this.video = Base64.getEncoder().encodeToString(video);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] getAudio() {
        if (audio != null) {
            return Base64.getDecoder().decode(audio.getBytes());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAudio(byte[] audio) {
        this.audio = Base64.getEncoder().encodeToString(audio);
    }

    @Override
    public String toString() {
        return super.toString() + "Attraction{" +
                "video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }
}
