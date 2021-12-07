package com.example.christiansoeappproject;

import android.os.Bundle;
import android.os.LimitExceededException;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.christiansoeappproject.model.Trip;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TripThemeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Trip> themeTrips = new ArrayList<>();
    private TextView tripName;
    private TextView tripInfo;
    private TextView tripTheme;
    private Trip currentTrip;
    private int index;
    private Bundle extras;
    private MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_trip);
        tripName = findViewById(R.id.tripName);
        tripInfo = findViewById(R.id.tripInfo);
        //tripTheme = findViewById(R.id.tripTheme);
        themeTrips = MainActivity.themeTrips;

        extras = getIntent().getExtras();
        if (extras!=null){
            index = extras.getInt("index");
        }
        currentTrip = themeTrips.get(index);

        tripName.setText(currentTrip.getName());
        tripInfo.setText(currentTrip.getInfo());
        //tripTheme.setText(currentTrip.themeToString(currentTrip.getTheme()));

        //MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tripMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

}
