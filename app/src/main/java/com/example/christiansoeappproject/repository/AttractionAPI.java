package com.example.christiansoeappproject.repository;

import com.example.christiansoeappproject.model.Attraction;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AttractionAPI {

    @GET("api/attraction/attractions")
    Call<List<Attraction>> getAttractions();
}
