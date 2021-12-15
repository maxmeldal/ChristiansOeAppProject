package com.example.christiansoeappproject.repository;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.IRestaurantEndpoint;
import com.example.christiansoeappproject.model.Restaurant;
import com.example.christiansoeappproject.Updatable;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository implements ICrudRepository<Restaurant>{

    public static List<Restaurant> restaurants;

    private static Updatable caller;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final IRestaurantEndpoint apiService = retrofit.create(IRestaurantEndpoint.class);

    public RestaurantRepository(Updatable updatable){
        caller = updatable;
        if (restaurants==null){
            restaurants = new ArrayList<>();
            System.out.println("Henter restauranter");
            readAll();
        }
    }

    @Override
    public void create(Restaurant restaurant) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        restaurants.add(restaurant);
        caller.update();

        Call<Restaurant> call = apiService.createRestaurant(restaurant);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(response.body() + " has been created!");
                readAll();
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public Restaurant readById(String id) {
        final Restaurant[] restaurants = {null};

        Call<Restaurant> call = apiService.readRestaurant(id);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.body()!=null){
                    System.out.println(response.body());
                    restaurants[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return restaurants[0];
    }

    @Override
    public void readAll() {
        Call<List<Restaurant>> call = apiService.readRestaurants();
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.body() != null){
                    restaurants.clear();
                    restaurants.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Restaurant newRestaurant) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        restaurants.removeIf(attraction -> attraction.getId().equals(newRestaurant.getId()));
        restaurants.add(newRestaurant);
        caller.update();

        Call<Restaurant> call = apiService.updateRestaurant(newRestaurant);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(newRestaurant + " has been updated!");
                readAll();
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        restaurants.removeIf(restaurant -> restaurant.getId().equals(id));
        caller.update();

        Call<Restaurant> call = apiService.deleteRestaurant(id);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println("Restaurant: " + id + " has been deleted!");
                readAll();
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}

