package com.example.christiansoeappproject.ui.admin.trip;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Theme;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionAdapter;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TripDetailActivity extends AppCompatActivity {
    private List<Attraction> attractions = new ArrayList<Attraction>();
    private boolean[] selectedAttraction;
    private ArrayList<Integer> dropdownList = new ArrayList<>();
    private List<Attraction> attractionDropdown = new ArrayList<>();
    //public static AttractionService service;
    private TextView dropdown;
    private Bundle extras;
    private String id;
    private EditText nameEditText;
    private EditText infoEditText;
    private Spinner theme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        dropdown = findViewById(R.id.attractionsView);
        nameEditText = findViewById(R.id.editTripName);
        infoEditText = findViewById(R.id.editTripInfo);
        theme = findViewById(R.id.themeSpinner);

        @SuppressLint("ResourceType")
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(TripDetailActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.themes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theme.setAdapter(myAdapter);

        extras = getIntent().getExtras();
        if (extras!=null){
            nameEditText.setText(extras.getString("name"));
            infoEditText.setText(extras.getString("info"));
            id = extras.getString("id");
        }

        String[] attractionsArr = new String[TripsActivity.attractionService.getAttractions().size()];
        for (int i = 0; i < attractionsArr.length; i++) {
            attractionsArr[i] = TripsActivity.attractionService.getAttractions().get(i).getName();
        }
        selectedAttraction = new boolean[attractionsArr.length];

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TripDetailActivity.this
                );

                builder.setTitle("Select attractions");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(attractionsArr, selectedAttraction, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        if(isChecked){
                           dropdownList.add(i);
                           attractionDropdown.add(TripsActivity.attractionService.getAttractions().get(i));
                        } else{
                            attractionDropdown.remove(TripsActivity.attractionService.getAttractions().get(i));
                            dropdownList.remove(i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int j = 0; j < dropdownList.size(); j++) {
                             stringBuilder.append(attractionsArr[dropdownList.get(j)]);
                             if(j != dropdownList.size() - 1){
                                stringBuilder.append(", ");
                             }
                        }
                        dropdown.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTripPressed(View view){
        TripsActivity.service.update(new Trip(id, nameEditText.getText().toString(), infoEditText.getText().toString(), getTheme(theme.getSelectedItem().toString()), attractionDropdown));
        TripsActivity.adapter.notifyDataSetChanged();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteTripPressed(View view){
        TripsActivity.service.delete(id);
        TripsActivity.adapter.notifyDataSetChanged();
        finish();
    }

    public int getTheme(String context){
        if(context.equals("Nature")){
            return 1;
        } else if(context.equals("History")){
            return 2;
        } else if(context.equals("War")){
            return 3;
        }
        return 4;
    }

    
}