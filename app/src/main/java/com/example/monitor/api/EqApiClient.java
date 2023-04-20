package com.example.monitor.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class EqApiClient {

    public interface EqService {
        @GET("all_hour.geojson")
        Call<EarthquakeJSONResponse> getEarthquakes();
    }

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    private EqService service;

    private static final EqApiClient apiClient = new EqApiClient();

    public static EqApiClient getInstance(){
        return apiClient;
    }

    private EqApiClient(){

    }

    public EqService getService(){
        if(service==null) {
            service = retrofit.create(EqService.class);
        }
        return service;
    }

}
