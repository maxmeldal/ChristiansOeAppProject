package com.example.christiansoeappproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.christiansoeappproject.databinding.ActivityMainBinding;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.service.DistanceService;
import com.example.christiansoeappproject.service.TripService;
import com.example.christiansoeappproject.ui.admin.AdminActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ActivityResultLauncher<String> permissionLauncher;//bruges til at søge om tilladelser

    public static String lastDistanceToFerry;
    public static Bitmap lastWeatherBitmap;

    @SuppressLint({"MissingPermission", "SetTextI18n"})
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

        /**
         * Ask for location permissions
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @SuppressLint({"MissingPermission", "SetTextI18n"})
                @Override
                public void onActivityResult(Boolean result) {
                    if (!result) {
                        //permission denied
                        Toast.makeText(MainActivity.this, "Tilladelse nødvendig!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            /*
             * shouldShowRequestPermissionRationale er her for at fortælle brugeren hvorfor man har brug for tilladelsen
             * Snackbar viser beskeden og hvor længe den skal vises.
             * setAction er en button med string som tekst og hvad der sker når der clikkes på den.
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Tilladelse nødvendig for location service", Snackbar.LENGTH_INDEFINITE).setAction("Giv Tilladelse", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //request permission ved at kalde permissionLauncher og fortælle hvilken tilladelse man ønsker
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }).show();
            } else {
                //request permission ved at kalde permissionLauncher og fortælle hvilken tilladelse man ønsker
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }
}