package com.example.mhike_native.ui.add_edit_observation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;

public class AddEditObservationViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<String> observationNameErrMsg;
    private final MutableLiveData<String> timestampErrMsg;
    private final MutableLiveData<Boolean> isObservationAdded;
    private final MutableLiveData<Boolean> isObservationUpdated;

    public AddEditObservationViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
        this.observationNameErrMsg = new MutableLiveData<>();
        this.timestampErrMsg = new MutableLiveData<>();
        this.isObservationAdded = new MutableLiveData<>();
        this.isObservationUpdated = new MutableLiveData<>();
    }

    public MutableLiveData<String> getObservationNameErrMsg() {
        return observationNameErrMsg;
    }

    public MutableLiveData<String> getTimestampErrMsg() {
        return timestampErrMsg;
    }

    public MutableLiveData<Boolean> getIsObservationAdded() {
        return isObservationAdded;
    }

    public MutableLiveData<Boolean> getIsObservationUpdated() {
        return isObservationUpdated;
    }

    public void addObservation(String name, String timestamp, String comments) {

        // Validation required fields
        // TODO: implement validation
        // Create Observation object and add to database
        // TODO: implement add logic
    }

    public void updateObservation(int id, String name, String timestamp, String comments) {

        // Validation required fields
        // TODO: implement validation
        // Create Observation object and update in database
        // TODO: implement update logic
    }
}