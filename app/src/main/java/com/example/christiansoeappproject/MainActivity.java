package com.example.christiansoeappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christiansoeappproject.ui.admin.AdminActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.christiansoeappproject.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_trips, R.id.navigation_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        button = findViewById(R.id.res_and_butik_BTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resOgButikBTN(view);
            }
        });


    }

    public void loginPressed(View view){
        Intent intent = new Intent(this, AdminActivity.class);
        EditText pas = findViewById(R.id.passwordEditText);
        String password = pas.getText().toString();
        String expected = "1";
        if(password.equals(expected)){
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
        }
    }
    public void resOgButikBTN(View view){
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(intent);
    }

}