package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.ui.Updatable;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionAdapter;

import java.util.ArrayList;
import java.util.List;

public class AttractionsActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private List<Attraction> attractions = new ArrayList<>();
    public static AttractionAdapter adapter;
    public static AttractionService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        service = new AttractionService(this);
        setupList();
    }

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
            startActivity(intent);
        });
    }

    public void create(View view){
        service.create(new Attraction(0,0, "New Attraction"));
        update();
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        setupList();
    }
}