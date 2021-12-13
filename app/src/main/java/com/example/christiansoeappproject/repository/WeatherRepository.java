package com.example.christiansoeappproject.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class WeatherRepository {

    public static Bitmap getWeather() throws IOException {


        Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?lat=55.3205598&lon=15.1847551&mode=html&appid=c4ab6e41a59879775af721340a5ebfff").get();

        String img = doc.select("img[src$=.png]").first().toString();
        img = img.substring(img.indexOf("http://"), img.indexOf(";)")-5);
        String imageUrl = img.replace("http", "https");
        URL url = new URL(imageUrl);
        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
    }
}
