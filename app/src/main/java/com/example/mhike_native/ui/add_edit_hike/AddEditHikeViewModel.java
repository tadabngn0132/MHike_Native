package com.example.mhike_native.ui.add_edit_hike;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddEditHikeViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<String> hikeNameErrMsg;
    private final MutableLiveData<String> locationErrMsg;
    private final MutableLiveData<String> dateErrMsg;
    private final MutableLiveData<String> lengthErrMsg;
    private final MutableLiveData<Boolean> isHikeAdded;
    private final MutableLiveData<Boolean> isHikeUpdated;

    private boolean hasEmptyError = false;
    private boolean hasAnotherError = false;
    private LocalDate parsedDate = null;
    private double parsedLength = 0.0;
    private boolean parsedParkingAvailable = false;

    public AddEditHikeViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = DatabaseHelper.getInstance(application);
        this.hikeNameErrMsg = new MutableLiveData<>();
        this.locationErrMsg = new MutableLiveData<>();
        this.dateErrMsg = new MutableLiveData<>();
        this.lengthErrMsg = new MutableLiveData<>();
        this.isHikeAdded = new MutableLiveData<>();
        this.isHikeUpdated = new MutableLiveData<>();
    }

    public LiveData<String> getHikeNameErrMsg() {
        return hikeNameErrMsg;
    }

    public LiveData<String> getLocationErrMsg() {
        return locationErrMsg;
    }

    public LiveData<String> getDateErrMsg() {
        return dateErrMsg;
    }

    public LiveData<String> getLengthErrMsg() {
        return lengthErrMsg;
    }

    public LiveData<Boolean> getIsHikeAdded() {
        return isHikeAdded;
    }

    public LiveData<Boolean> getIsHikeUpdated() {
        return isHikeUpdated;
    }

    private void validateAndParseInputs(String name, String location, String dateString, String lengthString, String parkingAvailableString) {
        hikeNameErrMsg.setValue("");
        locationErrMsg.setValue("");
        dateErrMsg.setValue("");
        lengthErrMsg.setValue("");

        if (name == null || name.trim().isEmpty()) {
            hikeNameErrMsg.setValue("Please enter a hike name");
            hasEmptyError = true;
        }

        if (location == null || location.trim().isEmpty()) {
            locationErrMsg.setValue("Please enter a location");
            hasEmptyError = true;
        }

        if (dateString == null || dateString.trim().isEmpty()) {
            dateErrMsg.setValue("Please enter a date");
            hasEmptyError = true;
        }

        if (lengthString == null || lengthString.trim().isEmpty()) {
            lengthErrMsg.setValue("Please enter the length of the hike");
            hasEmptyError = true;
        }

        if (hasEmptyError) {
            isHikeAdded.setValue(false);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            parsedDate = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            dateErrMsg.setValue("Please enter date with correct format (DD/MM/YYYY)");
            hasAnotherError = true;
        }

        try {
            parsedLength = Double.parseDouble(lengthString);
            if (parsedLength <= 0) {
                lengthErrMsg.setValue("Please enter a positive number for length");
                hasAnotherError = true;
            }
        } catch (NumberFormatException e) {
            lengthErrMsg.setValue("Please enter a valid number for length");
            hasAnotherError = true;
        }

        // Handle parking available parsing
        parsedParkingAvailable = "yes".equalsIgnoreCase(parkingAvailableString);

        if (hasAnotherError) {
            isHikeAdded.setValue(false);
        }
    }

    public void addHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        validateAndParseInputs(name, location, dateString, lengthString, parkingAvailableString);

        if (hasEmptyError || hasAnotherError) {
            return;
        }

        // Create Hike object and save to database
        Hike hike = new Hike(
            name.trim(),
            location.trim(),
            parsedDate,
            parsedParkingAvailable,
            parsedLength,
            difficulty,
            description == null ? "" : description.trim()
        );

        new Thread(() -> {
            long id = databaseHelper.addHike(hike);
            isHikeAdded.postValue(id != -1);
        }).start();
    }

    public void updateHike(long id, String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        validateAndParseInputs(name, location, dateString, lengthString, parkingAvailableString);

        if (hasEmptyError || hasAnotherError) {
            return;
        }

        // Create Hike object and update in database
        Hike hike = new Hike(
                id,
                name.trim(),
                location.trim(),
                parsedDate,
                parsedParkingAvailable,
                parsedLength,
                difficulty,
                description == null ? "" : description.trim()
        );

        new Thread (() -> {
            long updatedHikeId = databaseHelper.updateHike(hike);
            isHikeUpdated.postValue(updatedHikeId != -1);
        }).start();
    }

    public Hike getHikeById(long hikeId) {
        return databaseHelper.getHikeById(hikeId);
    }
}