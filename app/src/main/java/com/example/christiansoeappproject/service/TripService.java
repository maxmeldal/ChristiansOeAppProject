package com.example.christiansoeappproject.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Theme;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.repository.AttractionRepository;
import com.example.christiansoeappproject.repository.TripRepository;

import java.util.ArrayList;
import java.util.List;

public class TripService {
    TripRepository repo;

    public TripService(Updatable updatable) {
        repo  = new TripRepository(updatable);
    }

    public List<Trip> getTrips(){
        return TripRepository.trips;
    }

    public void create(Trip trip){
        repo.create(trip);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAttractionFromTrips(String id){
        repo.deleteAttractionFromTrips(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Trip trip){
        repo.update(trip);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }
}
