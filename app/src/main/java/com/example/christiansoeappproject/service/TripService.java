package com.example.christiansoeappproject.service;

import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.repository.TripRepository;

import java.util.List;

public class TripService {
    TripRepository repo = new TripRepository();

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
