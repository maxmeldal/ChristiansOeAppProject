package com.example.christiansoeappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ShowTripAttractionActivity extends AppCompatActivity {

    private TextView attractionName, attractionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip_attraction);
        attractionName = findViewById(R.id.attractionNameView);
        attractionDescription = findViewById(R.id.attractionDescriptionView);

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()){
            attractionName.setText(extras.getString("name"));
            attractionDescription.setText(extras.getString("description"));
            //System.out.println(extras.get("description"));
        }
    }
}