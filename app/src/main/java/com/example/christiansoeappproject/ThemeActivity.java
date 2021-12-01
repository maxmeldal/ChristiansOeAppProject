package com.example.christiansoeappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.christiansoeappproject.ui.admin.trip.TripsActivity;

public class ThemeActivity extends AppCompatActivity {

    CardView natureView;
    CardView historyView;
    CardView warView;
    CardView otherView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_themes);
        natureView = findViewById(R.id.natureTheme);
        historyView = findViewById(R.id.historyTheme);
        warView = findViewById(R.id.warTheme);
        otherView = findViewById(R.id.otherTheme);

        natureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(this, TripsByThemeActivity.class));
            }
        });







    }
}
