package com.example.christiansoeappproject.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherRepository {

    public static Bitmap getBitmap() {
        try {
            Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?lat=55.3205598&lon=15.1847551&mode=html&appid=c4ab6e41a59879775af721340a5ebfff").get();

            String img = doc.select("img[src$=.png]").first().toString();
            img = img.substring(img.indexOf("http://"), img.indexOf(";)") - 5);
            String imageUrl = img.replace("http", "https");
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
        }
        return null;
    }
}
