package com.example.christiansoeappproject.ui.admin.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.service.FacilityService;
import com.example.christiansoeappproject.service.RestaurantService;
import com.example.christiansoeappproject.ui.Updatable;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionDetailActivity;
import com.example.christiansoeappproject.ui.admin.facility.FacilityAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private List<Restaurant> restaurants = new ArrayList<>();
    public static RestaurantAdapter adapter;
    public static RestaurantService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        service = new RestaurantService(this);
        setupList();
    }

    private void setupList() {
        listView = findViewById(R.id.restaurantListView);
        restaurants = service.getRestaurants();
        adapter = new RestaurantAdapter(this, restaurants);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, AttractionDetailActivity.class);
            intent.putExtra("latitude", restaurants.get(i).getLatitude());
            intent.putExtra("longitude", restaurants.get(i).getLongitude());
            intent.putExtra("name", restaurants.get(i).getName());
            intent.putExtra("id", restaurants.get(i).getId());
            startActivity(intent);
        });
    }

    public void create(View view){
        service.create(new Restaurant(0,0, "New Restaurant","www.happyDolphin.dk",11.00,23.59));
    }

    @Override
    public void update() {
        restaurants = service.getRestaurants();
        adapter.notifyDataSetChanged();
    }
}
