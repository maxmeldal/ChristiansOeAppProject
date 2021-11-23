package com.example.christiansoeappproject.service;

import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.repository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;

public class FacilityService {

    FacilityRepository repo = new FacilityRepository();

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

    public void delete(String id){
        repo.delete(id);
    }

    public List<Facility> getFacilities() {
        List<Facility> facilities =new ArrayList<>();
        facilities.add(new Facility(1,2,"hej"));
        facilities.add(new Facility(1,2,"hej"));
        facilities.add(new Facility(1,2,"hej"));
        facilities.add(new Facility(1,2,"hej"));
        return facilities;
    }
}
