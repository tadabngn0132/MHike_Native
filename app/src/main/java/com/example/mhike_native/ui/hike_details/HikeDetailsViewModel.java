package com.example.mhike_native.ui.hike_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;
import com.example.mhike_native.models.Observation;

import java.util.List;

public class HikeDetailsViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<List<Observation>> observationsLiveData;
    public HikeDetailsViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = DatabaseHelper.getInstance(application);
        this.observationsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Observation>> getObservationsForHike(long hikeId) {
        new Thread(() -> {
            List<Observation> observationList = databaseHelper.getAllObservationsByHikeId(hikeId);
            observationsLiveData.postValue(observationList);
        }).start();
        return observationsLiveData;
    }

    protected boolean deleteHike(long hikeId) {
        long id = databaseHelper.deleteHike(hikeId);
        return id != -1;
    }

    public Hike getHikeById(long hikeId) {
        return databaseHelper.getHikeById(hikeId);
    }
}