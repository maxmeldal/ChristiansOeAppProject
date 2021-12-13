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
import android.widget.ImageView;
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
import com.example.christiansoeappproject.repository.WeatherRepository;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.service.DistanceService;
import com.example.christiansoeappproject.service.TripService;
import com.example.christiansoeappproject.ui.admin.AdminActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements Updatable, LocationListener {
    private ActivityMainBinding binding;

    public static TripService service;
    public static AttractionService attractionService;
    ActivityResultLauncher<String> permissionLauncher;//bruges til at søge om tilladelser
    private List<Trip> allTrips = new ArrayList<>();
    public static List<Trip> themeTrips = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationListener;
    TextView textViewDistance;
    DistanceService distanceService;
    private ImageView weatherImageView;

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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        registerLauncher();


        weatherImageView =findViewById(R.id.weatherImageView);
        Thread thread = new Thread(){
            public void run() {
                try {
                    Bitmap weatherBitMap = WeatherRepository.getWeather();
                    synchronized (this) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherImageView.setImageBitmap(weatherBitMap);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        };
        thread.start();


        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(@NonNull Location location) {
                textViewDistance.setText(Math.round(distanceService.distanceToFerry(location.getLatitude(), location.getLongitude()) * 1000) + "m");
            }
        };

        distanceService = new DistanceService();
        textViewDistance = findViewById(R.id.textViewDistance);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locationListener);
        }

        service = new TripService(this);
        attractionService = new AttractionService(this);
        //TODO: switch getTripsData with getTrips when it works
        allTrips = service.getTrips();
    }

   public void registerLauncher() {
       permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
           @SuppressLint({"MissingPermission", "SetTextI18n"})
           @Override
           public void onActivityResult(Boolean result) {
               if (result) {
                   if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       //permission granted, og hvad der så skal ske efterfølgende
                       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locationListener);
                   }
               } else {
                   //permission denied
                   Toast.makeText(MainActivity.this, "Tilladelse nødvendig!", Toast.LENGTH_LONG).show();
               }
           }
       });
   }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //textViewDistance.setText(Math.round(distanceService.distanceToFerry(location.getLatitude(), location.getLongitude()) * 1000) + "m");
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
        intent.putExtra("type", "restaurant");
        startActivity(intent);
    }

    public void facilitiesPressed(View view){
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        intent.putExtra("type", "facility");
        startActivity(intent);
    }

    //TODO:Remember to compress images or chose lower dif images

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void naturePressed(View view){
        Intent intent = new Intent(getApplicationContext(), TripThemeActivity.class);
        themeTrips.addAll(allTrips);
        themeTrips.removeIf(trip -> trip.getTheme() != 1);
        intent.putExtra("index", getRandomIndex());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void warPressed(View view){
        Intent intent = new Intent(getApplicationContext(), TripThemeActivity.class);
        themeTrips.addAll(allTrips);
        themeTrips.removeIf(trip -> trip.getTheme() != 3);
        intent.putExtra("index", getRandomIndex());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void otherPressed(View view){
        Intent intent = new Intent(getApplicationContext(), TripThemeActivity.class);
        themeTrips.addAll(allTrips);
        themeTrips.removeIf(trip -> trip.getTheme() != 4);
        intent.putExtra("index", getRandomIndex());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void historyPressed(View view){
        Intent intent = new Intent(getApplicationContext(), TripThemeActivity.class);
        themeTrips.addAll(allTrips);
        themeTrips.removeIf(trip -> trip.getTheme() != 2);
        intent.putExtra("index", getRandomIndex());
        startActivity(intent);
    }

    public int getRandomIndex(){
        Random random = new Random();
        int index;
        if(themeTrips.size() == 1){
            index = 0;
        }else{
            index = random.nextInt(themeTrips.size());
        }
        return index;
    }

    @Override
    public void update() {

    }
}