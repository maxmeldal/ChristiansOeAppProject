package com.example.christiansoeappproject.endpoint;
import com.example.christiansoeappproject.model.Trip;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ITripEndpoint {
    @POST("api/trip/create")
    Call<Trip> createTrip(@Body Trip trip);

    @GET("api/trip/{id}")
    Call<Trip> readTrip(@Path("id") String id);

    @GET("api/trip/trips")
    Call<List<Trip>> readTrips();

    @PUT("api/trip/update")
    Call<Trip> updateTrip(@Body Trip trip);

    @DELETE("api/trip/delete/{id}")
    Call<Trip> deleteTrip(@Path("id") String id);
}
