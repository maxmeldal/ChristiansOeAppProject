package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.example.christiansoeappproject.R;

public class AttractionDetailActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        nameEditText = findViewById(R.id.nameEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);

        extras = getIntent().getExtras();
        if (extras!=null){
            nameEditText.setText(extras.getString("name"));
            String latitude = String.valueOf(extras.getDouble("latitude"));
            String longitude = String.valueOf(extras.getDouble("longitude"));

            latitudeEditText.setText(extras.getString(latitude));
            longitudeEditText.setText(extras.getString(longitude));
        }
    }
}