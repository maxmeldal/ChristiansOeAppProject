package com.example.christiansoeappproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.service.FacilityService;
import com.example.christiansoeappproject.service.RestaurantService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.christiansoeappproject.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final MarkerOptions options = new MarkerOptions();
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;//bruges til at søge om tilladelser
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedPreferences;
    boolean info;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        extras = getIntent().getExtras();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        registerLauncher();

        //vi gemmer vores location på telefonens hukommelse
        sharedPreferences = this.getSharedPreferences("com.example.christiansoeappproject", MODE_PRIVATE);
        info = false;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //Casting ved at caste siger jeg at jeg er sikker på at det er Location service jeg vil have
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(@NonNull Location location) {

                info = sharedPreferences.getBoolean("info", false);
                if (!info) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.0f));
                    sharedPreferences.edit().putBoolean("info", true).apply();
                }
                mMap.setMyLocationEnabled(true);

            }
        };

        /*
        * hvis ikke man har fået tilladelse spørges der om det her.
        * ContextCompat for at det også skal kunne virke på tidligere versioner.
        * checkSelfPermission: tjekker tilladelserne
        */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
            * shouldShowRequestPermissionRationale er her for at fortælle brugeren hvorfor man har brug for tilladelsen
            * Snackbar viser beskeden og hvor længe den skal vises.
            * setAction er en button med string som tekst og hvad der sker når der clikkes på den.
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Tilladelse nødvendig for maps", Snackbar.LENGTH_INDEFINITE).setAction("Giv Tilladelse", new View.OnClickListener() {
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

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 17.0f));
            }
            //viser vores position med en blå cirkel
            mMap.setMyLocationEnabled(true);

        }

        if (extras.getString("type").equals("restaurant")) {
            ArrayList<Restaurant> resliste = extras.getParcelableArrayList("list");
            for (int i = 0; i < resliste.size(); i++) {
                LatLng latll = new LatLng(resliste.get(i).getLatitude(), resliste.get(i).getLongitude());
                String name = resliste.get(i).getName();
                mMap.addMarker(new MarkerOptions().position(latll).title(name));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        Optional<Restaurant> selected = resliste.stream().filter(restaurant -> restaurant.getName().equals(marker.getTitle())).findFirst();
                        if ((!selected.isPresent())) {
                            Toast.makeText(MapsActivity.this, "Could not select restaurant", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent(MapsActivity.this, ShowRestaurantActivity.class);
                        Restaurant restaurant = selected.get();
                        intent.putExtra("name", restaurant.getName());
                        intent.putExtra("close", restaurant.getClose());
                        intent.putExtra("open", restaurant.getOpen());
                        intent.putExtra("url", restaurant.getUrl());
                        intent.putExtra("description", restaurant.getDescription());
                        startActivity(intent);
                    }
                });
            }
        } else if (extras.getString("type").equals("facility")) {
            ArrayList<Facility> facilities = extras.getParcelableArrayList("list");
            for (Facility facility : facilities) {
                LatLng latll = new LatLng(facility.getLatitude(), facility.getLongitude());
                String name = facility.getName();
                mMap.addMarker(new MarkerOptions().position(latll).title(name));
            }
        }
    }

    // registerForActivityResult registrerer en results callback og har brug for en contract og en callback.
    public void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //permission granted, og hvad der så skal ske efterfølgende
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locationListener);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastLocation != null) {
                            LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 17));
                        }
                    }
                } else {
                    //permission denied
                    Toast.makeText(MapsActivity.this, "Tilladelse nødvendig!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //oprette en menu til map udseende
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}