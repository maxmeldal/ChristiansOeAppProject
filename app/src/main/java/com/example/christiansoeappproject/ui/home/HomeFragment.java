package com.example.christiansoeappproject.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christiansoeappproject.MainActivity;
import com.example.christiansoeappproject.MapsActivity;
import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.databinding.FragmentHomeBinding;
import com.example.christiansoeappproject.repository.WeatherRepository;
import com.example.christiansoeappproject.service.DistanceService;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private DistanceService distanceService;
    private ImageButton restaurantsButton, facilitiesButton;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        restaurantsButton = binding.restaurantsHomeButton;
        facilitiesButton = binding.facilitiesHomeButton;
        restaurantsButton.setOnClickListener(this);
        facilitiesButton.setOnClickListener(this);

        distanceService = new DistanceService();
        final TextView textView = binding.textViewDistance;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    textView.setText(Math.round(distanceService.distanceToFerry(location.getLatitude(), location.getLongitude()) * 1000) + "m");
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
        }

        final ImageView imageView = binding.weatherImageView;
        homeViewModel.getImage().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.restaurantsHomeButton):
                openMap("restaurant");
                break;
            case (R.id.facilitiesHomeButton):
                openMap("facility");
                break;
        }
    }

    public void openMap(String type){
        Intent intent = new Intent(getActivity(),MapsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}