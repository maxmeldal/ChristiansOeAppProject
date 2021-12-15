package com.example.christiansoeappproject.repository;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.IAttractionEndpoint;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.Updatable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttractionRepository implements ICrudRepository<Attraction>{

    public static List<Attraction> attractionList = new ArrayList<>();
    private static Updatable caller;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okClient())
            .build();
    final IAttractionEndpoint apiService = retrofit.create(IAttractionEndpoint.class);


    public void init(Updatable updatable) {
        caller = updatable;
        startListener();
    }

    private void startListener() {
        Call<List<Attraction>> call = apiService.readAttractions();
        call.enqueue(new Callback<List<Attraction>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (response.body() != null) {
                    attractionList.clear();
                    attractionList.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }


    @Override
    public void create(Attraction attraction) {
        attractionList.add(attraction);
        Call<Attraction> call = apiService.createAttraction(attraction);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println(response.body() + " has been created!");
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
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
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Attraction newAttraction) {
        attractionList.removeIf(attraction -> attraction.getId().equals(newAttraction.getId()));
        attractionList.add(newAttraction);

        Call<Attraction> call = apiService.updateAttraction(newAttraction);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println("updated: " + newAttraction);
                caller.update();
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {
        attractionList.removeIf(attraction -> attraction.getId().equals(id));

        Call<Attraction> call = apiService.deleteAttraction(id);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println("Attraction: " + id + " has been deleted!");
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private OkHttpClient okClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        return client;
    }
}
