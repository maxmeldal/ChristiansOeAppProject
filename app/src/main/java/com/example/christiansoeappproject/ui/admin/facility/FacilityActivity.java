package com.example.christiansoeappproject.ui.admin.facility;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.service.FacilityService;
import com.example.christiansoeappproject.ui.Updatable;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionAdapter;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FacilityActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private List<Facility> facilities = new ArrayList<>();
    public static FacilityAdapter adapter;
    public static FacilityService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        service = new FacilityService(this);
        setupList();
    }

    private void setupList() {
        listView = findViewById(R.id.facilityListView);
        facilities = service.getFacilities();
        adapter = new FacilityAdapter(this, facilities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, FacilityDetailActivity.class);
            intent.putExtra("latitude", facilities.get(i).getLatitude());
            intent.putExtra("longitude", facilities.get(i).getLongitude());
            intent.putExtra("name", facilities.get(i).getName());
            intent.putExtra("id", facilities.get(i).getId());
            startActivity(intent);
        });
    }

    public void create(View view){
        service.create(new Facility(0,0, "New Facility"));
        update();
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        setupList();
    }
}