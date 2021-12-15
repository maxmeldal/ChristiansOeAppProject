package com.example.christiansoeappproject.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.repository.AttractionRepository;

import java.util.List;

public class AttractionService{

    private AttractionRepository repo;

    public AttractionService(Updatable updatable) {
        repo  = new AttractionRepository(updatable);
    }

    public List<Attraction> getAttractions(){
        return AttractionRepository.attractions;
    }

    public void create(Attraction attraction){
        repo.create(attraction);
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
