package com.example.christiansoeappproject.repository;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.ITripEndpoint;
import com.example.christiansoeappproject.model.Trip;
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

public class TripRepository implements ICrudRepository<Trip>{
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .client(okClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static List<Trip> trips;
    private static Updatable caller;
    final ITripEndpoint apiService = retrofit.create(ITripEndpoint.class);

    public TripRepository (Updatable updatable){
        caller = updatable;
        if (trips==null){
            trips = new ArrayList<>();
            System.out.println("Henter ruter");
            readAll();
        }
    }

    @Override
    public void create(Trip trip) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        trips.add(trip);
        caller.update();

        Call<Trip> call = apiService.createTrip(trip);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println(response.body() + " has been created!");
                readAll();
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
    public void readAll() {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Trip trip) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        trips.removeIf(oldTrip -> oldTrip.getId().equals(trip.getId()));
        trips.add(trip);
        caller.update();

        Call<Trip> call = apiService.updateTrip(trip);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println(trip + " has been updated!");
                readAll();
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

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        trips.removeIf(trip -> trip.getId().equals(id));
        caller.update();

        Call<Trip> call = apiService.deleteTrip(id);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                System.out.println("Trip: " + id + " has been deleted!");
                readAll();
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAttractionFromTrips(String id){
        List<Trip> update = new ArrayList<>();
        for (Trip trip : trips) {
            if(trip.getAttractions().removeIf(attraction -> attraction.getId().equals(id))){
                update.add(trip);
            }
        }
        for (Trip trip : update) {
            update(trip);
        }
    }

    private OkHttpClient okClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        return client;
    }
}

