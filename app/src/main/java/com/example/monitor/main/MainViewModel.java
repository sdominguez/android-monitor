package com.example.monitor.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.monitor.Earthquake;
import com.example.monitor.api.RequestStatus;
import com.example.monitor.api.StatusWithDescription;
import com.example.monitor.database.EqDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final MainRepository repository;
    private MutableLiveData<StatusWithDescription> statusMutableLiveData = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        EqDatabase database = EqDatabase.getDatabase(application);
        repository = new MainRepository(database);
    }

    public LiveData<List<Earthquake>> getEqList() {
        return repository.getEqList();
    }

    public LiveData<StatusWithDescription> getStatusMutableLiveData(){
        return statusMutableLiveData;
    }

    public void downloadEarthquakes() {
        statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.LOADING,""));
        repository.downloadAndSaveEarthquakes(new MainRepository.DownloadStatusListener() {
            @Override
            public void downloadSuccess() {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.DONE,""));
            }

            @Override
            public void downloadError(String message) {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.LOADING, message));
            }
        });
    }

}
