package com.example.christiansoeappproject.ui.admin.facility;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionsActivity;

public class FacilityDetailActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Bundle extras;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detail);

        nameEditText = findViewById(R.id.facilityNameEditText);
        latitudeEditText = findViewById(R.id.facilityLatitudeEditText);
        longitudeEditText = findViewById(R.id.facilityLongitudeEditText);

        extras = getIntent().getExtras();
        if (extras!=null){
            nameEditText.setText(extras.getString("name"));

            latitudeEditText.setText(String.valueOf(extras.getDouble("latitude")));
            longitudeEditText.setText(String.valueOf(extras.getDouble("longitude")));

            id = extras.getString("id");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateFacilityPressed(View view){
        FacilityActivity.service.update(new Facility(id, Double.parseDouble(latitudeEditText.getText().toString()), Double.parseDouble(longitudeEditText.getText().toString()), nameEditText.getText().toString()));
        FacilityActivity.adapter.notifyDataSetChanged();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteFacilityPressed(View view){
        FacilityActivity.service.delete(id);
        //TODO - make use update instead
        FacilityActivity.adapter.notifyDataSetChanged();
        finish();
    }
}