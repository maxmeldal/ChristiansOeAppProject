package com.example.christiansoeappproject.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.repository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;

public class FacilityService {

    FacilityRepository repo;

    public FacilityService(Updatable updatable) {
        this.repo = new FacilityRepository(updatable);
    }

    public void create(Facility facility){
        repo.create(facility);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Facility facility){
        repo.update(facility);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }

    public List<Facility> getFacilities() {
        return FacilityRepository.facilities;
    }
}
