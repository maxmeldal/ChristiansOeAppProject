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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.christiansoeappproject.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Updatable {

    private GoogleMap mMap;
    private final MarkerOptions options = new MarkerOptions();
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedPreferences;
    boolean info;
    // ArrayList<LatLng> butikListe = new ArrayList<>();
    RestaurantService restaurantService;
    FacilityService facilityService;
    List<Restaurant> resliste;
    List<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantService = new RestaurantService(this);
        facilityService = new FacilityService(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resliste = restaurantService.getRestaurants();
        facilities = facilityService.getFacilities();


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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //Casting ved at caste siger jeg at jeg er sikker på at det er Location service jeg vil have
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                info = sharedPreferences.getBoolean("info", false);
                if (!info) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
                    sharedPreferences.edit().putBoolean("info", true).apply();
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Tilladelse nødvendig for maps", Snackbar.LENGTH_INDEFINITE).setAction("Giv Tilladelse", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

                    }
                }).show();
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 17));
            }
            //viser vores position med en blå cirkel
            mMap.setMyLocationEnabled(true);

        }

        if (getIntent().getExtras().getString("type").equals("restaurant")) {
            System.out.println("res listen er: " + resliste.size());
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
                        startActivity(intent);
                    }
                });
            }
        } else if (getIntent().getExtras().getString("type").equals("facility")) {
            for (Facility facility : facilities) {
                System.out.println("here");
                LatLng latll = new LatLng(facility.getLatitude(), facility.getLongitude());
                String name = facility.getName();
                mMap.addMarker(new MarkerOptions().position(latll).title(name));
            }
        }
    }

    public void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //permission granted
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


    @Override
    public void update() {
        resliste = restaurantService.getRestaurants();
        facilities = facilityService.getFacilities();
    }

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