package com.example.christiansoeappproject.endpoint;

import com.example.christiansoeappproject.model.Attraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IAttractionEndpoint {

    @GET("api/attraction/{id}")
    Call<Attraction> readAttraction(@Path("id") String id);

    @GET("api/attraction/attractions")
    Call<List<Attraction>> readAttractions();
}
