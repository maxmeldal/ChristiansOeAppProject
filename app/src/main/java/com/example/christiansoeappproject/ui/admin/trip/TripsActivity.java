package com.example.christiansoeappproject.ui.admin.trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.service.TripService;
import com.example.christiansoeappproject.Updatable;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private List<Trip> trips = new ArrayList<>();
    public static TripAdapter adapter;
    public static TripService service;
    public static AttractionService attractionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        service = new TripService(this);
        attractionService = new AttractionService(this);
        setupList();
    }

    public void setupList(){
        listView = findViewById(R.id.tripsListView);
        trips = service.getTrips();
        adapter = new TripAdapter(this, trips);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, TripDetailActivity.class);
            intent.putExtra("id", trips.get(i).getId());
            intent.putExtra("name", trips.get(i).getName());
            intent.putExtra("info", trips.get(i).getInfo());
            intent.putExtra("theme",trips.get(i).getTheme());
            intent.putExtra("attractions", trips.get(i).getAttractions().toString());
            System.out.println(trips);
            startActivity(intent);
        });
    }

    public void createPressed(View view){
        //Intent intent = new Intent(this, TripDetailActivity.class);
        //startActivity(intent);
        List<Attraction> list = new ArrayList<>();
        service.create(new Trip("Ny route", "...", 4, list));
        update();
    }

    @Override
    public void update() {
        //update();
        adapter.notifyDataSetChanged();
    }
}
