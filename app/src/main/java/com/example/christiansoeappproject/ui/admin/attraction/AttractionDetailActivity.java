package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.ui.Updatable;

public class AttractionDetailActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Bundle extras;
    private String id;

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

            id = extras.getString("id");

        }
    }

    public void updateAttractionPressed(View view){
        AttractionsActivity.service.update(new Attraction(id, Double.parseDouble(latitudeEditText.getText().toString()), Double.parseDouble(longitudeEditText.getText().toString()), nameEditText.getText().toString()));
        AttractionsActivity.adapter.notifyDataSetChanged();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAttractionPressed(View view){
        AttractionsActivity.service.delete(id);
        AttractionsActivity.adapter.notifyDataSetChanged();
        finish();
    }
}