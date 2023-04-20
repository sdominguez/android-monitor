package com.example.monitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.monitor.api.EarthquakeJSONResponse;
import com.example.monitor.api.EqApiClient;
import com.example.monitor.api.Feature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<List<Earthquake>> eqList = new MutableLiveData<>();

    public LiveData<List<Earthquake>> getEqList(){
        return eqList;
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

    public void getEarthquakes(){
        EqApiClient.EqService service = EqApiClient.getInstance().getService();
        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse > call, Response<EarthquakeJSONResponse > response) {
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());
                eqList.setValue(earthquakeList);
            }
            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) { }
        });
    }

    private List<Earthquake>parseEarthquakes(String responseString){
        ArrayList<Earthquake>eqList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(responseString);
            JSONArray featuresJSONArray = jsonResponse.getJSONArray("features");
            for(int i = 0; i < featuresJSONArray.length(); i++){
                JSONObject jsonFeature = featuresJSONArray.getJSONObject(i);
                String id = jsonFeature.getString("id");

                JSONObject jsonProperties = jsonFeature.getJSONObject("properties");
                double magnitude = jsonProperties.getDouble("mag");
                String place = jsonProperties.getString("place");
                long time = jsonProperties.getLong("time");

                JSONObject jsonGeometry = jsonFeature.getJSONObject("geometry");
                JSONArray coordinatesJSONArray = jsonGeometry.getJSONArray("coordinates");
                double longitude = coordinatesJSONArray.getDouble(0);
                double latitude = coordinatesJSONArray.getDouble(1);

                eqList.add(new Earthquake(id, place, magnitude, time, latitude, longitude));
            }

        } catch (JSONException e) {

        }
        return eqList;
    }


    public void getEarthquakesFake(){
        ArrayList<Earthquake> eqList = new ArrayList<>();
        eqList.add(new Earthquake("aaaa","CDMX",4.0,12365498L,105.23,98.127));
        eqList.add(new Earthquake("bbbb","La Paz",1.8,12365498L,105.23,98.127));
        eqList.add(new Earthquake("cccc","Barcelona",0.5,12365498L,105.23,98.127));
        eqList.add(new Earthquake("dddd","Buenos Aires",3.7,12365498L,105.23,98.127));
        eqList.add(new Earthquake("eeee","Washington D.C",2.8,12365498L,105.23,98.127));

        this.eqList.setValue(eqList);
    }
}
