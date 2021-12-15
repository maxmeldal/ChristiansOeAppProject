package com.example.christiansoeappproject.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christiansoeappproject.MainActivity;
import com.example.christiansoeappproject.MapsActivity;
import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.databinding.FragmentHomeBinding;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.repository.WeatherRepository;
import com.example.christiansoeappproject.service.DistanceService;
import com.example.christiansoeappproject.service.FacilityService;
import com.example.christiansoeappproject.service.RestaurantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener, Updatable {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private DistanceService distanceService;

    private RestaurantService restaurantService;
    private FacilityService facilityService;

    private ArrayList<Restaurant> restaurants;
    private ArrayList<Facility> facilities;

    private ImageButton restaurantsButton, facilitiesButton;
    private Button timeButton;
    private int hour, minute;
    private TextView departureTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        distanceService = new DistanceService();
        final TextView textView = binding.textViewDistance;
        if (MainActivity.lastDistanceToFerry!=null) textView.setText(MainActivity.lastDistanceToFerry);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    String distance = Math.round(distanceService.distanceToFerry(location.getLatitude(), location.getLongitude()) * 1000) + "m";
                    textView.setText(distance);
                    MainActivity.lastDistanceToFerry = distance;
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
        }

        final ImageView imageView = binding.weatherImageView;
        if(MainActivity.lastWeatherBitmap!=null) imageView.setImageBitmap(MainActivity.lastWeatherBitmap);
        homeViewModel.getImage().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                    MainActivity.lastWeatherBitmap = bitmap;
            }
        });

        restaurantsButton = binding.restaurantsHomeButton;
        facilitiesButton = binding.facilitiesHomeButton;
        timeButton = binding.timeButton;
        departureTime = binding.departureTime;

        timeButton.setOnClickListener(this);
        restaurantsButton.setOnClickListener(this);
        facilitiesButton.setOnClickListener(this);

        restaurantService = new RestaurantService(this);
        facilityService = new FacilityService(this);

        restaurants = (ArrayList<Restaurant>) restaurantService.getRestaurants();
        facilities = (ArrayList<Facility>) facilityService.getFacilities();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.restaurantsHomeButton):
                openMap("restaurant");
                break;
            case (R.id.facilitiesHomeButton):
                openMap("facility");
                break;
            case(R.id.timeButton):
                setDepartureTime();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDepartureTime(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle(" - Vælg færge afgang -");
        timePickerDialog.show();
        setTime();
    }

    public void openMap(String type){
        Intent intent = new Intent(getActivity(),MapsActivity.class);
        intent.putExtra("type", type);
        switch (type){
            case ("restaurant"):
                intent.putParcelableArrayListExtra("list", restaurants);
                break;
            case ("facility"):
                intent.putParcelableArrayListExtra("list", facilities);
                break;
        }
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTime(){
        LocalTime localTime = LocalTime.now();
        int currentHour = localTime.getHour();
        int currentMinute = localTime.getMinute();
        int milisInAMinute = 60000;
        long time = System.currentTimeMillis();

        Runnable update = new Runnable() {
            int departureHour;
            int departureMinute;
            public void run() {
                departureHour = currentHour - hour;
                departureMinute = currentMinute - minute;
                departureTime.setText(String.format(Locale.getDefault(), "%02d:%02d", departureHour, departureMinute));
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                update.run();
            }
        }, time % milisInAMinute, milisInAMinute);

        // This will update for the current minute, it will be updated again in at most one minute.
        update.run();
    }

    @Override
    public void update() {
        restaurants = (ArrayList<Restaurant>) restaurantService.getRestaurants();
        facilities = (ArrayList<Facility>) facilityService.getFacilities();
    }
}