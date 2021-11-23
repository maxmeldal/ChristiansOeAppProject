package com.example.christiansoeappproject.repository;
import com.example.christiansoeappproject.endpoint.IRestaurantEndpoint;
import com.example.christiansoeappproject.model.Restaurant;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository implements ICrudRepository<Restaurant>{
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final IRestaurantEndpoint apiService = retrofit.create(IRestaurantEndpoint.class);


    @Override
    public void create(Restaurant restaurant) {
        Call<Restaurant> call = apiService.createRestaurant(restaurant);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(response.body() + " has been created!");
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
    public List<Restaurant> readAll() {

        List<Restaurant> restaurantList = new ArrayList<>();

        Call<List<Restaurant>> call = apiService.readRestaurants();
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.body()!=null) {
                    System.out.println(response.body());
                    restaurantList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return restaurantList;
    }

    @Override
    public void update(Restaurant restaurant) {
        Call<Restaurant> call = apiService.updateRestaurant(restaurant);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(restaurant + " has been updated!");
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public void delete(String id) {

        Call<Restaurant> call = apiService.deleteRestaurant(id);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println("Restaurant: " + id + " has been deleted!");
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}

