package com.example.christiansoeappproject.repository;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.ITripEndpoint;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.model.Trip;
import com.example.christiansoeappproject.ui.Updatable;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripRepository implements ICrudRepository<Trip>{
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public List<Trip> trips = new ArrayList<>();
    private static Updatable caller;
    final ITripEndpoint apiService = retrofit.create(ITripEndpoint.class);


    public void init(Context context) {
        caller = (Updatable) context;
        startListener();
    }

    private void startListener() {
        Call<List<Trip>> call = apiService.readTrips();
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.body() != null) {
                    trips.clear();
                    trips.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public void create(Trip trip) {
        //adds to list before database
        trips.add(trip);
        Call<Trip> call = apiService.createTrip(trip);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println(response.body() + " has been created!");
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @Override
    public Trip readById(String id) {
        final Trip[] trips = {null};

        Call<Trip> call = apiService.readTrip(id);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.body()!=null){
                    System.out.println(response.body());
                    trips[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

        return trips[0];
    }

    @Override
    public List<Trip> readAll() {
        return null;
    }

//    @Override
//    public List<Trip> readAll() {
//
//        List<Trip> tripList = new ArrayList<>();
//
//        Call<List<Trip>> call = apiService.readTrips();
//        call.enqueue(new Callback<List<Trip>>() {
//            @Override
//            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
//                if (response.body()!=null) {
//                    System.out.println(response.body());
//                    tripList.addAll(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Trip>> call, Throwable t) {
//                System.out.println(t.toString());
//            }
//        });
//
//        return tripList;
//    }

    @Override
    public void update(Trip trip) {
        for (Trip oldTrip : trips) {
            if (oldTrip.getId().equals(trip.getId())){
                trips.remove(oldTrip);
                trips.add(trip);
            }
        }
        Call<Trip> call = apiService.updateTrip(trip);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println(trip + " has been updated!");
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {

        trips.removeIf(attraction -> attraction.getId().equals(id));

        Call<Trip> call = apiService.deleteTrip(id);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println("Trip: " + id + " has been deleted!");
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}

