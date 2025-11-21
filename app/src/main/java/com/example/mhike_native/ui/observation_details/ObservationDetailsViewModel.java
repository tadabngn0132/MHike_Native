package com.example.mhike_native.ui.observation_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Observation;

public class ObservationDetailsViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;

    public ObservationDetailsViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
    }
    public Observation getObservationById(long observationId) {
        return databaseHelper.getObservationById(observationId);
    }

    public boolean deleteObservation(long observationId) {
        long id = databaseHelper.deleteObservation(observationId);
        return id != -1;
    }
}