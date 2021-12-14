package com.example.christiansoeappproject.ui.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.TripThemeActivity;
import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.databinding.FragmentThemesBinding;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.service.TripService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThemeFragment extends Fragment implements View.OnClickListener, Updatable {

    private ThemeViewModel themeViewModel;
    private FragmentThemesBinding binding;
    private TripService service;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    
    private List<Trip> trips = new ArrayList<>();
    private ArrayList<Trip> themeTrips = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        themeViewModel =
                new ViewModelProvider(this).get(ThemeViewModel.class);

        binding = FragmentThemesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        service = new TripService(this);
        trips = service.getTrips();

        cardView1 = binding.natureTheme;
        cardView2 = binding.warTheme;
        cardView3 = binding.historyTheme;
        cardView4 = binding.otherTheme;

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.natureTheme):
                themePressed("nature");
                break;
            case(R.id.warTheme):
                themePressed("war");
                break;
            case(R.id.historyTheme):
                themePressed("history");
                break;
            case (R.id.otherTheme):
                themePressed("other");
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void themePressed(String s) {
        Intent intent = new Intent(getActivity(), TripThemeActivity.class);
        themeTrips.clear();
        themeTrips.addAll(trips);
        switch (s) {
            case ("nature"):
                themeTrips.removeIf(trip -> trip.getTheme() != 1);
                if (themeTrips.size() != 0) {
                    intent.putExtra("trip", getRandomTrip(themeTrips));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Ingen tilgængelige router", Toast.LENGTH_SHORT).show();
                }
                break;
            case ("history"):
                themeTrips.removeIf(trip -> trip.getTheme() != 2);
                if (themeTrips.size() != 0) {
                    intent.putExtra("trip", getRandomTrip(themeTrips));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Ingen tilgængelige router", Toast.LENGTH_SHORT).show();
                }
                break;
            case ("war"):
                themeTrips.removeIf(trip -> trip.getTheme() != 3);
                if (themeTrips.size() != 0) {
                    intent.putExtra("trip", getRandomTrip(themeTrips));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Ingen tilgængelige router", Toast.LENGTH_SHORT).show();
                }
                break;
            case ("other"):
                themeTrips.removeIf(trip -> trip.getTheme() != 4);
                if (themeTrips.size() != 0) {
                    intent.putExtra("trip", getRandomTrip(themeTrips));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Ingen tilgængelige router", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public Trip getRandomTrip(List<Trip> trips){
        if(trips.size() == 0){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(trips.size());
        return trips.get(index);
    }

    @Override
    public void update() {
        themeTrips = (ArrayList<Trip>) service.getTrips();
    }
}