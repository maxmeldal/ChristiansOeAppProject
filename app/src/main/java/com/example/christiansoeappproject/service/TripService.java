package com.example.christiansoeappproject.service;

import android.content.Context;

import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Theme;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.repository.AttractionRepository;
import com.example.christiansoeappproject.repository.TripRepository;

import java.util.ArrayList;
import java.util.List;

public class TripService {
    TripRepository repo;

    public TripService(Context context) {
        repo  = new TripRepository();
        repo.init(context);
    }

    public List<Trip> getTrips(){
        List<Attraction> attractions = new ArrayList<>();
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("name", "info", Theme.None, attractions));
        trips.add(new Trip("name", "info", Theme.None, attractions));
        trips.add(new Trip("name", "info", Theme.None, attractions));
        trips.add(new Trip("name", "info", Theme.None, attractions));
        trips.add(new Trip("name", "info", Theme.None, attractions));
        return trips;
    }

    public void create(Trip trip){
        repo.create(trip);
    }

    public Trip readById(String id){
        return  repo.readById(id);
    }

    public List<Trip> readAll(){
        return repo.readAll();
    }

    public void update(Trip trip){
        repo.update(trip);
    }
    public void delete(String id){
        repo.delete(id);
    }
}
