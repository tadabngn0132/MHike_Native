package com.example.mhike_native.ui.add_edit_observation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
    private final MutableLiveData<Hike> hikeNameAndDateLiveData;
    private boolean hasEmptyError = false;
    private boolean hasFormatError = false;
    private LocalDateTime parsedTimestamp = null;

    public AddEditObservationViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
        this.observationNameErrMsg = new MutableLiveData<>();
        this.timestampErrMsg = new MutableLiveData<>();
        this.isObservationAdded = new MutableLiveData<>();
        this.isObservationUpdated = new MutableLiveData<>();
        this.hikeNameAndDateLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Hike> getHikeNameAndDateLiveData() {
        return hikeNameAndDateLiveData;
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

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH);

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
            Hike hike = databaseHelper.getHikeById(hikeId);
            hikeNameAndDateLiveData.postValue(hike);
        }).start();
    }

    public void updateObservation(int id, String name, String hikeDateString, String timestamp, String comments, long hikeId) {

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

}