package com.example.christiansoeappproject.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionsActivity;
import com.example.christiansoeappproject.ui.admin.restaurant.RestaurantActivity;
import com.example.christiansoeappproject.ui.admin.trip.TripsActivity;
import com.example.christiansoeappproject.ui.admin.facility.FacilityActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void attractionsPressed(View view){
        Intent intent = new Intent(this, AttractionsActivity.class);
        startActivity(intent);
    }

    public void tripsPressed(View view){
        Intent intent = new Intent(this, TripsActivity.class);
        startActivity(intent);
    }

    public void facilitiesPressed(View view){
        Intent intent = new Intent(this, FacilityActivity.class);
        startActivity(intent);
    }
    public void restaurantsPressed(View view){
        Intent intent = new Intent(this, RestaurantActivity.class);
        startActivity(intent);
    }
}