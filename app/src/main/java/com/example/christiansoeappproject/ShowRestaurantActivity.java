package com.example.christiansoeappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowRestaurantActivity extends AppCompatActivity {

    TextView textViewName, textViewHours,textViewURL, textViewDescription;
    ImageView imageViewRes;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant);

        textViewName = findViewById(R.id.textViewName);
        textViewURL = findViewById(R.id.textViewURL);
        textViewHours = findViewById(R.id.textViewOpenClose);
        textViewDescription = findViewById(R.id.textViewDescription);
        imageViewRes = findViewById(R.id.imageViewRestaurant);

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()){
            textViewName.setText(extras.getString("name"));
            textViewHours.setText(extras.getDouble("open") + "-" + extras.getDouble("close"));
            String url = extras.getString("url");
            if (url!=null && !url.isEmpty()){
                textViewURL.setText(url);
                textViewURL.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                textViewURL.setText("Der er ikke et link til denne restaurant");
            }
            textViewDescription.setText(extras.getString("description"));
        }
    }
}