package com.example.christiansoeappproject.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.ui.admin.attraction.AttractionsActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void attractionsPressed(View view){
        Intent intent = new Intent(this, AttractionsActivity.class);
        startActivity(intent);
    }
}