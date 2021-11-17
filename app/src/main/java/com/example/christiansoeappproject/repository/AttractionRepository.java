package com.example.christiansoeappproject.repository;

import androidx.recyclerview.widget.RecyclerView;

import com.example.christiansoeappproject.model.Attraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.disposables.CompositeDisposable;

public class AttractionRepository implements ICrudRepository<Attraction>{

    public static final String BASE_URL = "https://10.0.2.2:5001/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    //CompositeDisposable compositeDisposable;

    @Override
    public void create(Attraction attraction) {

    }

    @Override
    public Attraction readById(String id) {
        return null;
    }

    @Override
    public List<Attraction> readAll() {

        List<Attraction> attractionList = new ArrayList<>();

        final AttractionAPI apiService = retrofit.create(AttractionAPI.class);
        Call<List<Attraction>> call = apiService.getAttractions();
        call.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                /*if (response.body()!=null) {
                    attractionList.addAll(response.body());
                }*/
                System.out.println(response.body());
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

    public void test(){
        try {
            Document doc = Jsoup.connect("https://localhost:5001/api/attraction/attractions").get();
            System.out.println(doc.body());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
