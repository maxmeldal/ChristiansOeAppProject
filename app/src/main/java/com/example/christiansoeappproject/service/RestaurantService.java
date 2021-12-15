package com.example.christiansoeappproject.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.Updatable;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.repository.RestaurantRepository;

import java.util.List;

public class RestaurantService {
    RestaurantRepository repo;

    public RestaurantService(Updatable updatable){
        repo = new RestaurantRepository(updatable);
    }
    public List<Restaurant> getRestaurants(){
        return RestaurantRepository.restaurants;
    }

    public void create(Restaurant restaurant){
        repo.create(restaurant);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Restaurant restaurant){
        repo.update(restaurant);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(String id){
        repo.delete(id);
    }


}
