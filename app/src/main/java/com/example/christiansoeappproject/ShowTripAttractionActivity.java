package com.example.christiansoeappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ShowTripAttractionActivity extends AppCompatActivity {

    TextView attractionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip_attraction);
        attractionName = findViewById(R.id.attractionNameView);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()){
            attractionName.setText(bundle.getString("name"));
        }
    }
}