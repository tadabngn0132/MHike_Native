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

    public AddEditHikeViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(getApplication());
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

    public void addHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            hikeNameErrMsg.setValue("Please enter a hike name");
            isHikeAdded.setValue(false);
            return;
        }

        if (location == null || location.trim().isEmpty()) {
            locationErrMsg.setValue("Please enter a location");
            isHikeAdded.setValue(false);
            return;
        }

        // Handle date parsing and validation logic
        if (dateString == null || dateString.trim().isEmpty()) {
            dateErrMsg.setValue("Please enter a date");
            isHikeAdded.setValue(false);
            return;
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            dateErrMsg.setValue("Please enter date with correct format (DD/MM/YYYY)");
            isHikeAdded.setValue(false);
            return;
        }

        // Handle length parsing and validation logic
        if (lengthString == null || lengthString.trim().isEmpty()) {
            lengthErrMsg.setValue("Please enter the length of the hike");
            isHikeAdded.setValue(false);
            return;
        }

        double length;
        try {
            length = Double.parseDouble(lengthString);
            if (length <= 0) {
                lengthErrMsg.setValue("Please enter a positive number for length");
                isHikeAdded.setValue(false);
                return;
            }
        } catch (NumberFormatException e) {
            lengthErrMsg.setValue("Please enter a valid number for length");
            isHikeAdded.setValue(false);
            return;
        }

        // Handle parking available parsing
        boolean parkingAvailable = "yes".equalsIgnoreCase(parkingAvailableString);

        // Create Hike object and save to database
        Hike hike = new Hike(
            name.trim(),
            location.trim(),
            date,
            parkingAvailable,
            length,
            difficulty,
            description == null ? "" : description.trim()
        );

        new Thread(() -> {
            long id = databaseHelper.addHike(hike);
            isHikeAdded.postValue(id != -1);
        }).start();
    }

    public void updateHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            hikeNameErrMsg.setValue("Please enter a hike name");
            isHikeUpdated.setValue(false);
            return;
        }

        if (location == null || location.trim().isEmpty()) {
            locationErrMsg.setValue("Please enter a location");
            isHikeUpdated.setValue(false);
            return;
        }

        // Handle date parsing and validation logic
        if (dateString == null || dateString.trim().isEmpty()) {
            dateErrMsg.setValue("Please enter a date");
            isHikeUpdated.setValue(false);
            return;
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            dateErrMsg.setValue("Please enter date with correct format (DD/MM/YYYY)");
            isHikeUpdated.setValue(false);
            return;
        }

        // Handle length parsing and validation logic
        if (lengthString == null || lengthString.trim().isEmpty()) {
            lengthErrMsg.setValue("Please enter the length of the hike");
            isHikeUpdated.setValue(false);
            return;
        }

        double length;
        try {
            length = Double.parseDouble(lengthString);
            if (length <= 0) {
                lengthErrMsg.setValue("Please enter a positive number for length");
                isHikeUpdated.setValue(false);
                return;
            }
        } catch (NumberFormatException e) {
            lengthErrMsg.setValue("Please enter a valid number for length");
            isHikeUpdated.setValue(false);
            return;
        }

        // Handle parking available parsing
        boolean parkingAvailable = "yes".equalsIgnoreCase(parkingAvailableString);

        // Create Hike object and update in database
        Hike hike = new Hike(
                name.trim(),
                location.trim(),
                date,
                parkingAvailable,
                length,
                difficulty,
                description == null ? "" : description.trim()
        );

        new Thread (() -> {
            long id = databaseHelper.updateHike(hike);
            isHikeUpdated.setValue(id != -1);
        }).start();
    }
}