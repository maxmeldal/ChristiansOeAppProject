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

    @Override
    public String toString() {
        if (audio==null && video==null){
            return super.toString() + "Attraction{" +
                    "video='null" + '\'' +
                    ", audio='null" + '\'' +
                    '}';
        }
        if (audio == null){
            return super.toString() + "Attraction{" +
                    "video='" + video.substring(0, 10) + '\'' +
                    ", audio='null" + '\'' +
                    '}';
        }
        if (video==null){
            return super.toString() + "Attraction{" +
                    "video='null" + '\'' +
                    ", audio='" + audio.substring(0, 10) + '\'' +
                    '}';
        }
        return super.toString() + "Attraction{" +
                "video='" + video.substring(0, 10) + '\'' +
                ", audio='" + audio.substring(0, 10) + '\'' +
                '}';
    }
}
