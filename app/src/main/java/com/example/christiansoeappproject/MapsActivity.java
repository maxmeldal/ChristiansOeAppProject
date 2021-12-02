package com.example.christiansoeappproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.repository.RestaurantRepository;
import com.example.christiansoeappproject.service.RestaurantService;
import com.example.christiansoeappproject.ui.Updatable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.christiansoeappproject.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Updatable {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedPreferences;
    boolean info;
   // ArrayList<LatLng> butikListe = new ArrayList<>();
    RestaurantService restaurantService;

//    LatLng sirenehuset = new LatLng(55.32007928389102, 15.186848922143419);
//    LatLng christiansøGlas = new LatLng(55.319892754738305, 15.186976409042497);
//    LatLng christiansøKiosk = new LatLng(55.32082021048631, 15.186639479372074);
//    LatLng christiansøGæstgiveri = new LatLng(55.32072522067318, 15.186493780058843);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantService = new RestaurantService(this);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
                if (!info){
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,17));
                    sharedPreferences.edit().putBoolean("info",true).apply();
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(),"Tilladelse nødvendig for maps",Snackbar.LENGTH_INDEFINITE).setAction("Giv Tilladelse", new View.OnClickListener() {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10000,locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null){
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,17));
            }
            //viser vores position med en blå cirkel
            mMap.setMyLocationEnabled(true);

        }

       // addMarkersToMap();
        List<Restaurant> resliste = restaurantService.getRestaurants();

        System.out.println("res listen er: " + resliste.size());
        for (int i = 0; i<resliste.size(); i++){
            LatLng latll = new LatLng(resliste.get(i).getLatitude(),resliste.get(i).getLongitude());
            String name = new String(resliste.get(i).getName());
            mMap.addMarker(new MarkerOptions().position(latll).title(name));
        }





        // sirenehuset 55.32007928389102, 15.186848922143419
        // christiansø glas 55.319892754738305, 15.186976409042497
        // Christiansø kiosk 55.32082021048631, 15.186639479372074
        // Christiansø Gæstgiveri 55.32072522067318, 15.186493780058843
//        butikListe.add(new LatLng(55.32007928389102, 15.186848922143419));
//        butikListe.add(new LatLng(55.319892754738305, 15.186976409042497));
//        butikListe.add(new LatLng(55.32082021048631, 15.186639479372074));
//        butikListe.add(new LatLng(55.32072522067318, 15.186493780058843));
//        for (LatLng latLng: butikListe) {
//            options.position(latLng);
//            options.title("Butikker og restauranter");
//            mMap.addMarker(options);
//
//        }

        //lat, long
        //55.320169382013646, 15.188192360514055
        //LatLng christiansoe = new LatLng(55.320169382013646, 15.188192360514055);
//        mMap.addMarker(new MarkerOptions()
//                .position(christiansoe)
//                .title("Marker i Christiansoe"))
//                .showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(christiansoe));

        // Kongens Bastion 55.31792723678517, 15.188481968614353
   //     LatLng kongensBastion = new LatLng(55.31792723678517, 15.188481968614353);
//        mMap.addMarker(new MarkerOptions()
//                .position(christiansoe)
//                .title("Kongens Bastion"))
//                .showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(kongensBastion));
    }
    public void registerLauncher(){
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    if (ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

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
                    Toast.makeText(MapsActivity.this,"Tilladelse nødvendig!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addMarkersToMap(){
        //mMap.clear();
        List<Restaurant> resliste = restaurantService.getRestaurants();

        System.out.println("res listen har størrelsen: " + resliste.size());
        for (int i = 0; i<resliste.size(); i++){
            LatLng latll = new LatLng(resliste.get(i).getLatitude(),resliste.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(latll).title("Fisk"));
        }
    }


    @Override
    public void update() {

    }
}