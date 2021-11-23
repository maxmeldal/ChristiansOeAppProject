package com.example.christiansoeappproject.endpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ITimeEndpoint {

    @GET("api/distance/?distance={distance}")
    Call<Double> readDistance(@Path("distance") double distance);

}
