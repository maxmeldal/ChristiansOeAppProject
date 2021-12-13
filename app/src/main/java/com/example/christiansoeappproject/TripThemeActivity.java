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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.christiansoeappproject.databinding.ActivityMapsBinding;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.model.Trip;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TripThemeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Trip> themeTrips = new ArrayList<>();
    private TextView tripName;
    private TextView tripInfo;
    private TextView tripTheme;
    private Trip currentTrip;
    private int index;
    private Bundle extras;
    private ActivityMapsBinding binding;
    private ActivityResultLauncher<String> permissionLauncher;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleMap mMap;
    boolean info;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private SharedPreferences sharedPreferences;
    private MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_trip);
        tripName = findViewById(R.id.tripName);
        tripInfo = findViewById(R.id.tripInfo);
        //tripTheme = findViewById(R.id.tripTheme);
        themeTrips = MainActivity.themeTrips;

        extras = getIntent().getExtras();
        if (extras!=null){
            index = extras.getInt("index");
        }
        currentTrip = themeTrips.get(index);

        tripName.setText(currentTrip.getName());
        tripInfo.setText(currentTrip.getInfo());
        //tripTheme.setText(currentTrip.themeToString(currentTrip.getTheme()));

        //MAP
        getSupportActionBar().setTitle("Christians Ø");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tripMap);
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
            LatLng userLocation = new LatLng(55.320535766, 15.1887);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.10F));

            //viser vores position med en blå cirkel
            mMap.setMyLocationEnabled(true);
        }
        PolylineOptions options = new PolylineOptions();
        options.color(Color.argb(0,0,0,0));
        LatLng startLatLng = null;
        LatLng endLatLng = null;
        if(currentTrip.getAttractions().size() > 0){
            for (int i = 0; i< currentTrip.getAttractions().size(); i++){
                LatLng dest = new LatLng(currentTrip.getAttractions().get(i).getLatitude(),currentTrip.getAttractions().get(i).getLongitude());
                String name = currentTrip.getAttractions().get(i).getName();
                mMap.addMarker(new MarkerOptions().position(dest).title(name));
                //polyline = mMap.addPolyline(new PolylineOptions().clickable(true).add(
                //         new LatLng(currentTrip.getAttractions().get(i).getLatitude(), currentTrip.getAttractions().get(i).getLongitude())
                //));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        Optional<Attraction> selected = currentTrip.getAttractions().stream().filter(attraction -> attraction.getName().equals(marker.getTitle())).findFirst();
                        if ((!selected.isPresent())) {
                            Toast.makeText(TripThemeActivity.this, "Could not select attraction", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent(TripThemeActivity.this, ShowTripAttractionActivity.class);
                        Attraction attraction = selected.get();
                        intent.putExtra("name", attraction.getName());
                        intent.putExtra("description", attraction.getDescription());
                        startActivity(intent);
                    }
                });

                //LatLng latLng = new LatLng(currentTrip.getAttractions().get(i).getLatitude(), currentTrip.getAttractions().get(i).getLongitude());

                /*if(i == 0){
                   startLatLng = latLng;
                }

                if(i == currentTrip.getAttractions().size() - 1){
                   endLatLng = latLng;
                }*/
                //options.add(latLng);
                //String url = getDirectionsURL(startLatLng, endLatLng);
                //Download
            }
        }
        //mMap.addPolyline(options);
        //polyline.setTag('A');
        //assert polyline != null;
        //stylePolyline(polyline);

    }

    //private void stylePolyline(Polyline polyline) {
    //}


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

    public String getDirectionsURL(LatLng origin, LatLng dest){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        String output = "json";

        String url = "http://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

}
