package com.example.christiansoeappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowRestaurantActivity extends AppCompatActivity {

    TextView textViewName, textViewHours,textViewURL, textViewDescription;
    ImageView imageViewRes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant);

        textViewName = findViewById(R.id.textViewName);
        textViewURL = findViewById(R.id.textViewURL);
        textViewURL.setMovementMethod(LinkMovementMethod.getInstance());
        textViewHours = findViewById(R.id.textViewOpenClose);
        textViewDescription = findViewById(R.id.textViewDescription);
        imageViewRes = findViewById(R.id.imageViewRestaurant);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()){
            textViewName.setText(bundle.getString("name"));
            textViewName.setText(bundle.getString("open-close"));
            textViewName.setText(bundle.getString("open"));
            textViewName.setText(bundle.getString("url"));
        }




    }
}