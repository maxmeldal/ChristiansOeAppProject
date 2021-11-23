package com.example.christiansoeappproject.service;

import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.repository.FacilityRepository;

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
}
