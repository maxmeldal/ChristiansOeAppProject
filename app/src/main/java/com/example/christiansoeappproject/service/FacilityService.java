package com.example.christiansoeappproject.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.repository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;

public class FacilityService {

    FacilityRepository repo;

    public FacilityService(Context context) {
        this.repo = new FacilityRepository();
        repo.init(context);
    }

    public void create(Facility facility){
        repo.create(facility);
    }

    public Facility readById(String id){
        return  repo.readById(id);
    }

    public List<Facility> readAll(){
        return repo.readAll();
    }

    public void update(Facility facility){
        repo.update(facility);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }

    public List<Facility> getFacilities() {
        return repo.facilities;
    }
}
