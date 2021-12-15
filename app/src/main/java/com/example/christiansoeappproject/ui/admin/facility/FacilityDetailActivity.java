package com.example.christiansoeappproject.ui.admin.facility;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.ui.admin.SelectLocationOnMapActivity;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionsActivity;

public class FacilityDetailActivity extends AppCompatActivity {
    private static final int REQUEST_GET_LATLNG = 1;
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

    public void locationFacilityPressed(View view){
        Intent intent = new Intent(this, SelectLocationOnMapActivity.class);
        double lat = Double.parseDouble(latitudeEditText.getText().toString());
        double longg = Double.parseDouble(longitudeEditText.getText().toString());
        if (lat!=0 && longg!=0){
            intent.putExtra("lat", lat);
            intent.putExtra("long", longg);
        }
        startActivityForResult(intent, REQUEST_GET_LATLNG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_LATLNG && resultCode==1){
            Bundle dataExtras = data.getExtras();
            latitudeEditText.setText(String.valueOf(dataExtras.getDouble("lat")));
            longitudeEditText.setText(String.valueOf(dataExtras.getDouble("long")));
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