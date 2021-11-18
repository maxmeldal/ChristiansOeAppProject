package com.example.christiansoeappproject.service;

import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.repository.AttractionRepository;

import java.util.List;

public class AttractionService{

    AttractionRepository repo = new AttractionRepository();

    public void create(Attraction attraction){
        repo.create(attraction);
    }

    public Attraction readById(String id){
        return  repo.readById(id);
    }

    public List<Attraction> readAll(){
        return repo.readAll();
    }

    public void update(Attraction attraction){
        repo.update(attraction);
    }

    public void delete(String id){
        repo.delete(id);
    }
}
