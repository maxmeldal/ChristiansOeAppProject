package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.service.TripService;
import com.example.christiansoeappproject.Updatable;

import java.util.ArrayList;
import java.util.List;

public class AttractionsActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private List<Attraction> attractions = new ArrayList<>();
    public static AttractionAdapter adapter;
    public static AttractionService service;
    public static TripService tripService;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        service = new AttractionService(this);
        tripService = new TripService(this);
        setupList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setupList(){
        listView = findViewById(R.id.attractionsListView);
        attractions = service.getAttractions();
        adapter = new AttractionAdapter(this, attractions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, AttractionDetailActivity.class);
            intent.putExtra("latitude", attractions.get(i).getLatitude());
            intent.putExtra("longitude", attractions.get(i).getLongitude());
            intent.putExtra("name", attractions.get(i).getName());
            intent.putExtra("id", attractions.get(i).getId());
            intent.putExtra("image", attractions.get(i).getImage());
            intent.putExtra("description", attractions.get(i).getDescription());
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void create(View view){
        service.create(new Attraction(0,0, "New Attraction", "Description"));
        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        setupList();
    }
}