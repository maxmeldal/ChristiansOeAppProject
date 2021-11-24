package com.example.christiansoeappproject.endpoint;
import com.example.christiansoeappproject.model.Facility;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IFacilityEndpoint {
    @POST("api/facility/create")
    Call<Facility> createFacility(@Body Facility facility);

    @GET("api/facility/{id}")
    Call<Facility> readFacility(@Path("id") String id);

    @GET("api/facility/facilities")
    Call<List<Facility>> readFacilities();

    @PUT("api/facility/update")
    Call<Facility> updateFacility(@Body Facility facility);

    @DELETE("api/facility/delete/{id}")
    Call<Facility> deleteFacility(@Path("id") String id);
}
