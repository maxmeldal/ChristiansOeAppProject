package com.example.christiansoeappproject.repository;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.christiansoeappproject.endpoint.IFacilityEndpoint;
import com.example.christiansoeappproject.model.Facility;
import com.example.christiansoeappproject.Updatable;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacilityRepository implements ICrudRepository<Facility>{
    public static List<Facility> facilities;
    private static Updatable caller;
    private Context mainContext;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final IFacilityEndpoint apiService = retrofit.create(IFacilityEndpoint.class);

    public void init (Updatable updatable){
        caller = updatable;
        if (facilities.isEmpty()){
            System.out.println("Henter faciliteter");
            readAll();
        }
    }

    public FacilityRepository(Updatable updatable) {
        caller = updatable;
        if (updatable instanceof Context && mainContext!=null){
            mainContext = (Context) updatable;
        }
        if (facilities==null){
            facilities = new ArrayList<>();
            System.out.println("Henter faciliteter");
            readAll();
        }

    }

    @Override
    public void create(Facility facility) {
        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        facilities.add(facility);
        caller.update();

        Call<Facility> call = apiService.createFacility(facility);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println(response.body() + " has been created!");
                readAll();
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println("Error creating facility: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved oprettelse af facilitet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void readAll(){
        Call<List<Facility>> call = apiService.readFacilities();
        call.enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.body()!=null) {
                    facilities.clear();
                    facilities.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                System.out.println("Error reading facilities: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Timeout indlæsning af faciliteter", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Facility newFacility) {
        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        facilities.removeIf(facility -> facility.getId().equals(newFacility.getId()));
        facilities.add(newFacility);
        caller.update();

        Call<Facility> call = apiService.updateFacility(newFacility);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println(newFacility + " has been updated!");
                readAll();
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println("Error updating facility: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved opdatering af facilitet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        facilities.removeIf(facility -> facility.getId().equals(id));
        caller.update();

        Call<Facility> call = apiService.deleteFacility(id);
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                System.out.println("Facility: " + id + " has been deleted!");
                readAll();
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                System.out.println("Error deleting facility: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved sletning af facilitet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
