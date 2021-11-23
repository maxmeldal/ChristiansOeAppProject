package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionAdapter;

import java.util.ArrayList;
import java.util.List;

public class AttractionsActivity extends AppCompatActivity {

    private ListView listView;
    private List<Attraction> attractions = new ArrayList<>();
    private static AttractionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        setupList();
    }

    public void setupList(){
        listView = findViewById(R.id.attractionsListView);
        fillList();
        adapter = new AttractionAdapter(this, attractions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, AttractionDetailActivity.class);
            intent.putExtra("latitude", attractions.get(i).getLatitude());
            intent.putExtra("longitude", attractions.get(i).getLongitude());
            intent.putExtra("name", attractions.get(i).getName());
            startActivity(intent);
        });
    }

    public void fillList(){
        attractions.add(new Attraction(12, 12, "Hej"));
        attractions.add(new Attraction(13, 12, "Name"));
        attractions.add(new Attraction(14, 12, "Name"));
    }
}