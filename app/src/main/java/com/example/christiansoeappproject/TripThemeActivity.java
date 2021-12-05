package com.example.christiansoeappproject;

import android.os.Bundle;
import android.os.LimitExceededException;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.christiansoeappproject.model.Trip;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TripThemeActivity extends AppCompatActivity{

    private Random random = new Random();
    private List<Trip> themeTrips = new ArrayList<>();
    private TextView tripName;
    private TextView tripInfo;
    private TextView tripTheme;
    private Trip currentTrip;
    private int index;
    private Bundle extras;
    private TextView tripAttractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_trip);
        tripName = findViewById(R.id.tripName);
        tripInfo = findViewById(R.id.tripInfo);
        tripTheme = findViewById(R.id.tripTheme);
        themeTrips = MainActivity.themeTrips;

        extras = getIntent().getExtras();
        if (extras!=null){
            index = extras.getInt("index");
        }
        currentTrip = themeTrips.get(index);

        tripName.setText(currentTrip.getName());
        tripInfo.setText(currentTrip.getInfo());
        tripTheme.setText(currentTrip.themeToString(currentTrip.getTheme()));

    }
}
