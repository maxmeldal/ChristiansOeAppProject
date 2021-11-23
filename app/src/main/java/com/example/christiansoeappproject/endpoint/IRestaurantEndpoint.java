package com.example.christiansoeappproject.endpoint;
import com.example.christiansoeappproject.model.Restaurant;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRestaurantEndpoint {
    @POST("api/restaurant/create")
    Call<Restaurant> createRestaurant(@Body Restaurant restaurant);

    @GET("api/restaurant/{id}")
    Call<Restaurant> readRestaurant(@Path("id") String id);

    @GET("api/restaurant/attractions")
    Call<List<Restaurant>> readRestaurants();

    @PUT("api/restaurant/update")
    Call<Restaurant> updateRestaurant(@Body Restaurant restaurant);

    @DELETE("api/restaurant/delete/{id}")
    Call<Restaurant> deleteRestaurant(@Path("id") String id);
}
