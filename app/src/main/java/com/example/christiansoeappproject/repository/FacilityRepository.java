package com.example.christiansoeappproject.repository;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.IFacilityEndpoint;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.ui.Updatable;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacilityRepository implements ICrudRepository<Facility>{
    public List<Facility> facilities = new ArrayList<>();
    private static Updatable caller;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final IFacilityEndpoint apiService = retrofit.create(IFacilityEndpoint.class);

    public void init (Context context){
        caller = (Updatable) context;
        startListener();
    }

    @Override
    public void create(Facility facility) {
        facilities.add(facility);
        Call<Facility> call = apiService.createFacility(facility);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println(response.body() + " has been created!");
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public Facility readById(String id) {
        final Facility[] facilities = {null};

        Call<Facility> call = apiService.readFacility(id);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                if (response.body()!=null){
                    System.out.println(response.body());
                    facilities[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return facilities[0];
    }

    public void startListener(){
        Call<List<Facility>> call = apiService.readFacilities();
        call.enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.body()!=null) {
                    facilities.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public List<Facility> readAll() {

        List<Facility> facilityList = new ArrayList<>();

        Call<List<Facility>> call = apiService.readFacilities();
        call.enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.body()!=null) {
                    System.out.println(response.body());
                    facilityList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return facilityList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Facility newFacility) {
        facilities.removeIf(facility -> facility.getId().equals(newFacility.getId()));
        facilities.add(newFacility);

        Call<Facility> call = apiService.updateFacility(newFacility);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println(newFacility + " has been updated!");
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {
        facilities.removeIf(facility -> facility.getId().equals(id));

        Call<Facility> call = apiService.deleteFacility(id);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println("Facility: " + id + " has been deleted!");
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}
