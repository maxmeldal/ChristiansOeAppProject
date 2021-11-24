package com.example.christiansoeappproject.service;

import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    RestaurantRepository repo = new RestaurantRepository();

    public void create(Restaurant restaurant){
        repo.create(restaurant);
    }

    public Restaurant readById(String id){
        return  repo.readById(id);
    }

    public List<Restaurant> readAll(){
        return repo.readAll();
    }

    public void update(Restaurant restaurant){
        repo.update(restaurant);
    }

    public void delete(String id){
        repo.delete(id);
    }

    public List<Restaurant> getRestaurants(){
        List<Restaurant>restaurants = new ArrayList<>();
        return restaurants;
    }
}
