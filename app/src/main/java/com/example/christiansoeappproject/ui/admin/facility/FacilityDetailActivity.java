package com.example.christiansoeappproject.ui.admin.facility;

import androidx.appcompat.app.AppCompatActivity;

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
            String latitude = String.valueOf(extras.getDouble("latitude"));
            String longitude = String.valueOf(extras.getDouble("longitude"));

            latitudeEditText.setText(extras.getString(latitude));
            longitudeEditText.setText(extras.getString(longitude));

            id = extras.getString("id");
        }
    }

    public void updatePressed(View view){
        FacilityActivity.service.update(new Facility(id, Double.parseDouble(latitudeEditText.getText().toString()), Double.parseDouble(longitudeEditText.getText().toString()), nameEditText.getText().toString()));
        FacilityActivity.adapter.notifyDataSetChanged();
        finish();
    }

    public void deletePressed(View view){
        FacilityActivity.service.delete(id);
        //TODO - make use update instead
        FacilityActivity.adapter.notifyDataSetChanged();
        finish();
    }
}