package com.example.christiansoeappproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LimitExceededException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.AsyncTaskLoader;

import com.example.christiansoeappproject.databinding.ActivityMapsBinding;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.ui.dashboard.ThemeFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TripThemeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Trip> themeTrips = new ArrayList<>();
    private TextView tripName;
    private TextView tripInfo;
    private TextView tripTheme;
    private Trip currentTrip;
    private Bundle extras;
    private ActivityMapsBinding binding;
    private ActivityResultLauncher<String> permissionLauncher;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleMap mMap;
    boolean info;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_trip);
        tripName = findViewById(R.id.tripName);
        tripInfo = findViewById(R.id.tripInfo);

        extras = getIntent().getExtras();
        if (extras!=null){
            currentTrip = extras.getParcelable("trip");
        }

        tripName.setText(currentTrip.getName());
        tripInfo.setText(currentTrip.getInfo());

        //MAP
        getSupportActionBar().setTitle("Christians Ø");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tripMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        registerLauncher();

        //vi gemmer vores location på telefonens hukommelse
        sharedPreferences = this.getSharedPreferences("com.example.christiansoeappproject",MODE_PRIVATE);
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 16.0f));
            }
            //viser vores position med en blå cirkel
            mMap.setMyLocationEnabled(true);
        }
        addMarkers();

    }

    public void addMarkers() {
        List<Attraction> attractions = currentTrip.getAttractions();
        PolylineOptions polylineOptions = new PolylineOptions();
        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(15), new Dash(30), new Gap(15));

        //add route start location
        LatLng start = new LatLng(55.32045, 15.18763);
        mMap.addMarker(new MarkerOptions()
                .position(start)
                .title("Rute start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        polylineOptions.add(start);

        if(attractions.size() > 0){
            for (int i = 0; i< attractions.size(); i++){
                LatLng dest = new LatLng(attractions.get(i).getLatitude(),attractions.get(i).getLongitude());
                String name = attractions.get(i).getName();
                mMap.addMarker(new MarkerOptions().position(dest).title(i+": "+name));
                polylineOptions.add(dest);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        Optional<Attraction> selected = attractions.stream().filter(attraction -> attraction.getName().equals(marker.getTitle())).findFirst();
                        if ((!selected.isPresent())) {
                            Toast.makeText(TripThemeActivity.this, "Kunne ikke vælge attraktion", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent(TripThemeActivity.this, ShowTripAttractionActivity.class);
                        Attraction attraction = selected.get();
                        intent.putExtra("name", attraction.getName());
                        intent.putExtra("description", attraction.getDescription());
                        startActivity(intent);
                    }
                });
            }
        }
        LatLng end = new LatLng(55.32045, 15.18763);
        polylineOptions.add(end);
        polylineOptions.pattern(pattern);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);
    }

    public void registerLauncher(){
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    if (ContextCompat.checkSelfPermission(TripThemeActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                        //permission granted
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10000,locationListener);

                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastLocation != null){
                            LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,17));
                        }
                    }

                }else {
                    //permission denied
                    Toast.makeText(TripThemeActivity.this,"Tilladelse nødvendig!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
