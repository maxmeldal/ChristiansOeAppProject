package com.example.christiansoeappproject.endpoint;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDistanceEndpoint {

    @GET("api/distance/{lat}&{lon}")
    Call<Double> readDistance(@Path("lat")double lat, @Path("lon") double lon);

}
