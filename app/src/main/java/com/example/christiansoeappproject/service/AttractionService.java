package com.example.christiansoeappproject.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.repository.AttractionRepository;

import java.util.List;

public class AttractionService{

    AttractionRepository repo;

    public AttractionService(Updatable updatable) {
        repo  = new AttractionRepository();
        repo.init(updatable);

    }

    public List<Attraction> getAttractions(){
        return AttractionRepository.attractionList;
    }

    public void create(Attraction attraction){
        repo.create(attraction);
    }

    public Attraction readById(String id){
        return  repo.readById(id);
    }

    public List<Attraction> readAll(){
        return repo.readAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Attraction attraction){
        repo.update(attraction);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }
}
