package com.example.christiansoeappproject.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    RestaurantRepository repo;

    public RestaurantService(Updatable updatable){
        repo = new RestaurantRepository();
        repo.init(updatable);
    }
    public List<Restaurant> getRestaurants(){
        return RestaurantRepository.restaurantList;
    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }


}
