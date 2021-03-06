package com.example.christiansoeappproject.endpoint;

import com.example.christiansoeappproject.model.Attraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAttractionEndpoint {

    @POST("api/attraction/create")
    Call<Attraction> createAttraction(@Body Attraction attraction);

    @GET("api/attraction/{id}")
    Call<Attraction> readAttraction(@Path("id") String id);

    @GET("api/attraction/attractions")
    Call<List<Attraction>> readAttractions();

    @PUT("api/attraction/update")
    Call<Attraction> updateAttraction(@Body Attraction attraction);

    @DELETE("api/attraction/delete/{id}")
    Call<Attraction> deleteAttraction(@Path("id") String id);
}
