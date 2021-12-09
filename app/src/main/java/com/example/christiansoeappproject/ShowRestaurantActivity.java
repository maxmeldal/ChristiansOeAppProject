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

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()){
            textViewName.setText(bundle.getString("name"));
            textViewHours.setText(bundle.getDouble("open") + "-" + bundle.getDouble("close"));
            String url = bundle.getString("url");
            if (url!=null && !url.isEmpty()){
                textViewURL.setText(url);
                textViewURL.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                textViewURL.setText("Der er ikke et link til denne restaurant");
            }
        }
    }
}