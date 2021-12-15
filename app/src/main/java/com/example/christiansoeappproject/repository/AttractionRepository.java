package com.example.christiansoeappproject.repository;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

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

    public static List<Attraction> attractions;
    private static Updatable caller;
    private Context mainContext;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okClient())
            .build();
    final IAttractionEndpoint apiService = retrofit.create(IAttractionEndpoint.class);

    public AttractionRepository(Updatable updatable){
        caller = updatable;
        if (updatable instanceof Context && mainContext!=null){
            mainContext = (Context) updatable;
        }
        if (attractions==null){
            attractions = new ArrayList<>();
            System.out.println("Henter attraktioner");
            readAll();
        }
    }

    @Override
    public void create(Attraction attraction) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        attractions.add(attraction);
        caller.update();

        Call<Attraction> call = apiService.createAttraction(attraction);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println(response.body() + " has been created!");
                readAll();
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println("Error creating attraction: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved oprettelse af attraktion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void readAll() {
        Call<List<Attraction>> call = apiService.readAttractions();
        call.enqueue(new Callback<List<Attraction>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (response.body() != null) {
                    attractions.clear();
                    attractions.addAll(response.body());
                }
                caller.update();
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                System.out.println("Error reading attractions: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Timeout indlæsning af attraktioner", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Attraction newAttraction) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        attractions.removeIf(attraction -> attraction.getId().equals(newAttraction.getId()));
        attractions.add(newAttraction);
        caller.update();

        Call<Attraction> call = apiService.updateAttraction(newAttraction);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println("updated: " + newAttraction);
                readAll();
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println("Error updating attraction: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved opdatering af attraktion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void delete(String id) {

        //Opdater liste før database, for hurtigere loadingtider
        //Normalt dårlig praksis, men grundet Azure gratis pakkes loading tider, en nødvendighed
        attractions.removeIf(attraction -> attraction.getId().equals(id));
        caller.update();

        Call<Attraction> call = apiService.deleteAttraction(id);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                System.out.println("Attraction: " + id + " has been deleted!");
                readAll();
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                System.out.println("Error deleting attraction: " + t.toString());
                if (mainContext!=null){
                    Toast.makeText(mainContext, "Fejl ved sletning af attraktion", Toast.LENGTH_LONG).show();
                }
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
