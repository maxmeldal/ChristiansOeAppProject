package com.example.christiansoeappproject.ui.admin.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.service.RestaurantService;
import com.example.christiansoeappproject.Updatable;

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
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra("latitude", restaurants.get(i).getLatitude());
            intent.putExtra("longitude", restaurants.get(i).getLongitude());
            intent.putExtra("name", restaurants.get(i).getName());
            intent.putExtra("id", restaurants.get(i).getId());
            intent.putExtra("url", restaurants.get(i).getUrl());
            intent.putExtra("open", restaurants.get(i).getOpen());
            intent.putExtra("close", restaurants.get(i).getClose());
            intent.putExtra("description", restaurants.get(i).getDescription());

            startActivity(intent);
        });
    }

    public void create(View view){
        service.create(new Restaurant(0,0, "New Restaurant","www.happyDolphin.dk",08.00,20.00, "description"));
        update();
    }

    @Override
    public void update() {
        restaurants = service.getRestaurants();
        adapter.notifyDataSetChanged();
        setupList();
    }
}
