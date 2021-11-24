package com.example.christiansoeappproject.ui.admin.restaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {
    private EditText nameEditText, latitudeEditText, longitudeEditText,restaurantURLEditText ,openEditText, closeEditText;

    private Bundle extras;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        nameEditText = findViewById(R.id.restaurantNameEditText);
        openEditText = findViewById(R.id.restaurantOpenEditText);
        closeEditText = findViewById(R.id.restaurantCloseEditText);
        restaurantURLEditText = findViewById(R.id.restaurantURLEditText);
        latitudeEditText = findViewById(R.id.restaurantLatitudeEditText);
        longitudeEditText = findViewById(R.id.restaurantLongitudeEditText);


        extras = getIntent().getExtras();
        if (extras!=null){
            nameEditText.setText(extras.getString("name"));
            nameEditText.setText(extras.getString("url"));

            String latitude = String.valueOf(extras.getDouble("latitude"));
            String longitude = String.valueOf(extras.getDouble("longitude"));
            latitudeEditText.setText(extras.getString(latitude));
            longitudeEditText.setText(extras.getString(longitude));

            String open = String.valueOf(extras.getDouble("opens"));
            String close = String.valueOf(extras.getDouble("closes"));
            openEditText.setText(extras.getString(open));
            openEditText.setText(extras.getString(close));


            id = extras.getString("id");
        }
    }


    public void updatePressed(View view){
        RestaurantActivity.service.update(new Restaurant(id, Double.parseDouble(latitudeEditText.getText().toString())
                                                            , Double.parseDouble(longitudeEditText.getText().toString())
                                                            ,nameEditText.getText().toString()
                                                            ,restaurantURLEditText.getText().toString()
                                                            ,Double.parseDouble(openEditText.getText().toString())
                                                            ,Double.parseDouble(closeEditText.getText().toString())));
        RestaurantActivity.adapter.notifyDataSetChanged();
        finish();
    }

    public void deletePressed(View view){
        RestaurantActivity.service.delete(id);
        //TODO - make use update instead
        RestaurantActivity.adapter.notifyDataSetChanged();
        finish();
    }
}
