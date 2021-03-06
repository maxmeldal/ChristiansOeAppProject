package com.example.christiansoeappproject.ui.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.databinding.ActivitySelectLocationOnMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class SelectLocationOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySelectLocationOnMapBinding binding;
    private Bundle extras;
    private LatLng prevLocation;
    private LatLng newLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectLocationOnMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();
        if (extras!=null){
            double lat = extras.getDouble("lat");
            double longg = extras.getDouble("long");
            prevLocation = new LatLng(lat, longg);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        if (prevLocation!=null){
            mMap.addMarker(new MarkerOptions().position(prevLocation).title("Gammel placering"));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.3203, 15.1888), 15.5f));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Ny placering"));
                Snackbar.make(binding.getRoot(), "Placering ??ndret", Snackbar.LENGTH_INDEFINITE).setAction("Gem", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newLocation = latLng;
                        setResult(1, new Intent().putExtra("lat", newLocation.latitude).putExtra("long", newLocation.longitude));
                        finish();
                    }
                }).show();
            }
        });
    }
}