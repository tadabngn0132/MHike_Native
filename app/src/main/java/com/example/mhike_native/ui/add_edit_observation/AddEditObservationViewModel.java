package com.example.mhike_native.ui.add_edit_observation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;
import com.example.mhike_native.models.Observation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class AddEditObservationViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<String> observationNameErrMsg;
    private final MutableLiveData<String> timestampErrMsg;
    private final MutableLiveData<Boolean> isObservationAdded;
    private final MutableLiveData<Boolean> isObservationUpdated;
    private boolean hasEmptyError = false;
    private boolean hasFormatError = false;
    private LocalDateTime parsedTimestamp = null;
    private final MutableLiveData<Hike> hikeLiveData;
    private final MutableLiveData<Observation> observationLiveData;

    public AddEditObservationViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = DatabaseHelper.getInstance(application);
        this.observationNameErrMsg = new MutableLiveData<>();
        this.timestampErrMsg = new MutableLiveData<>();
        this.isObservationAdded = new MutableLiveData<>();
        this.isObservationUpdated = new MutableLiveData<>();
        this.hikeLiveData = new MutableLiveData<>();
        this.observationLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Hike> getHikeLiveData() {
        return hikeLiveData;
    }

    public MutableLiveData<Observation> getObservationLiveData() {
        return observationLiveData;
    }

    private void validateInputs(String name, String hikeDateString, String timestamp) {
        observationNameErrMsg.setValue("");
        timestampErrMsg.setValue("");

        if (name == null || name.trim().isEmpty()) {
            observationNameErrMsg.setValue("Please enter a observation name");
            hasEmptyError = true;
        }

        if (timestamp == null || timestamp.trim().isEmpty()) {
            timestampErrMsg.setValue("Please enter a timestamp");
            hasEmptyError = true;
        }

        if (hasEmptyError) {
            isObservationAdded.setValue(false);
            return;
        }

        String mergedTimestamp = hikeDateString + " " + timestamp.trim();

        System.out.println(mergedTimestamp);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.ENGLISH);

            parsedTimestamp = LocalDateTime.parse(mergedTimestamp.trim(), formatter);

        } catch (DateTimeParseException e) {
            timestampErrMsg.setValue("Please enter a valid timestamp");
            hasFormatError = true;
        }

        if (hasFormatError) {
            isObservationAdded.setValue(false);
        }
    }

    public void addObservation(String name, String hikeDateString, String timestamp, String comments, long hikeId) {
        // Validate required fields
        validateInputs(name, hikeDateString, timestamp);

        if (hasEmptyError || hasFormatError) {
            return;
        }

        // Create Observation object and add to database
        Observation observation = new Observation(
                name.trim(),
                parsedTimestamp,
                comments == null ? "" : comments.trim(),
                hikeId
        );

        new Thread(() -> {
            long id = databaseHelper.addObservation(observation);
            isObservationAdded.postValue(id != -1);
        }).start();
    }

    public void getHikeNameAndDateByHikeId(long hikeId) {
        new Thread(() -> {
            Hike hike = databaseHelper.getHikeNameAndDateByHikeId(hikeId);
            hikeLiveData.postValue(hike);
        }).start();
    }

    public void updateObservation(long id, String name, String hikeDateString, String timestamp, String comments, long hikeId) {

        // Validation required fields
        validateInputs(name, hikeDateString, timestamp);

        if (hasEmptyError || hasFormatError) {
            return;
        }

        // Create Observation object and update in database
        Observation observation = new Observation(
                id,
                name.trim(),
                parsedTimestamp,
                comments == null ? "" : comments.trim(),
                hikeId
        );

        new Thread(() -> {
            long updatedObservationId = databaseHelper.updateObservation(observation);
            isObservationUpdated.postValue(updatedObservationId != -1);
        }).start();
    }

    public void getObservationById(long observationId) {
        new Thread(() -> {
            Observation observation = databaseHelper.getObservationById(observationId);
            observationLiveData.postValue(observation);
        }).start();
    }
}