package com.example.christiansoeappproject.repository;

import com.example.christiansoeappproject.endpoint.IAttractionEndpoint;
import com.example.christiansoeappproject.model.Attraction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttractionRepository implements ICrudRepository<Attraction>{

    public static final String BASE_URL = "https://csrestapp.azurewebsites.net/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final IAttractionEndpoint apiService = retrofit.create(IAttractionEndpoint.class);


    @Override
    public void create(Attraction attraction) {

    }

    @Override
    public Attraction readById(String id) {
        final Attraction[] attraction = {null};

        Call<Attraction> call = apiService.readAttraction(id);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                if (response.body()!=null){
                    System.out.println(response.body());
                    attraction[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return attraction[0];
    }

    @Override
    public List<Attraction> readAll() {

        List<Attraction> attractionList = new ArrayList<>();

        Call<List<Attraction>> call = apiService.readAttractions();
        call.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (response.body()!=null) {
                    attractionList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return attractionList;
    }

    @Override
    public void update(Attraction attraction) {

    }

    @Override
    public void delete(String id) {

    }
}
