package com.example.monitor.main;

import androidx.lifecycle.LiveData;

import com.example.monitor.Earthquake;
import com.example.monitor.api.EarthquakeJSONResponse;
import com.example.monitor.api.EqApiClient;
import com.example.monitor.api.Feature;
import com.example.monitor.database.EqDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private final EqDatabase database;

    public interface DownloadStatusListener{
        void downloadSuccess();
        void downloadError(String message);
    }

    public MainRepository(EqDatabase database) {
        this.database = database;
    }

    public LiveData<List<Earthquake>> getEqList() {
        return database.eqDAO().getEarthquakes();
    }

    public void downloadAndSaveEarthquakes(DownloadStatusListener downloadStatusListener) {
        EqApiClient.EqService service = EqApiClient.getInstance().getService();

        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call, Response<EarthquakeJSONResponse> response) {
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());

                EqDatabase.databaseWriteExecutor.execute(() -> {
                    database.eqDAO().insertAll(earthquakeList);
                });

                downloadStatusListener.downloadSuccess();
            }

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {
                downloadStatusListener.downloadError(t.getMessage());
            }
        });
    }

    private List<Earthquake> getEarthquakesWithMoshi(EarthquakeJSONResponse body) {
        ArrayList<Earthquake> eqList = new ArrayList<>();

        List<Feature> features = body.getFeatures();
        for (Feature feature: features) {
            String id = feature.getId();
            double magnitude = feature.getProperties().getMagnitude();
            String place = feature.getProperties().getPlace();
            long time = feature.getProperties().getTime();

            double longitude = feature.getGeometry().getLongitude();
            double latitude = feature.getGeometry().getLatitude();
            Earthquake earthquake = new Earthquake(id, place, magnitude, time,
                    latitude, longitude);
            eqList.add(earthquake);
        }

        return eqList;
    }

}
