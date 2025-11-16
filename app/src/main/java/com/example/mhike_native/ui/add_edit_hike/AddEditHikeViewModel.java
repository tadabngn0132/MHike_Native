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
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> hikeNameErrMsg;
    private final MutableLiveData<String> locationErrMsg;
    private final MutableLiveData<String> dateErrMsg;
    private final MutableLiveData<String> lengthErrMsg;

    public AddEditHikeViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(getApplication());
        this.errorMessage = new MutableLiveData<>();
        this.hikeNameErrMsg = new MutableLiveData<>();
        this.locationErrMsg = new MutableLiveData<>();
        this.dateErrMsg = new MutableLiveData<>();
        this.lengthErrMsg = new MutableLiveData<>();
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
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

    protected boolean addHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
//            errorMessage.setValue("Please enter a hike name");
            hikeNameErrMsg.setValue("Please enter a hike name");
            return false;
        }

        if (location == null || location.trim().isEmpty()) {
//            errorMessage.setValue("Please enter a location");
            locationErrMsg.setValue("Please enter a location");
            return false;
        }

        // Handle date parsing and validation logic
        if (dateString == null || dateString.trim().isEmpty()) {
//            errorMessage.setValue("Please enter a date");
            dateErrMsg.setValue("Please enter a date");
            return false;
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
//            errorMessage.setValue("Please enter date with correct format (DD/MM/YYYY)");
            dateErrMsg.setValue("Please enter date with correct format (DD/MM/YYYY)");
            return false;
        }

        // Handle length parsing and validation logic
        if (lengthString == null || lengthString.trim().isEmpty()) {
//            errorMessage.setValue("Please enter the length of the hike");
            lengthErrMsg.setValue("Please enter the length of the hike");
            return false;
        }

        double length;
        try {
            length = Double.parseDouble(lengthString);
            if (length <= 0) {
//                errorMessage.setValue("Please enter a positive number for length");
                lengthErrMsg.setValue("Please enter a positive number for length");
                return false;
            }
        } catch (NumberFormatException e) {
//            errorMessage.setValue("Please enter a valid number for length");
            lengthErrMsg.setValue("Please enter a valid number for length");
            return false;
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

        long id = databaseHelper.addHike(hike);

        return id != -1;
    }

    protected boolean updateHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            hikeNameErrMsg.setValue("Please enter a hike name");
            return false;
        }

        if (location == null || location.trim().isEmpty()) {
            locationErrMsg.setValue("Please enter a location");
            return false;
        }

        // Handle date parsing and validation logic
        if (dateString == null || dateString.trim().isEmpty()) {
            dateErrMsg.setValue("Please enter a date");
            return false;
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            dateErrMsg.setValue("Please enter date with correct format (DD/MM/YYYY)");
            return false;
        }

        // Handle length parsing and validation logic
        if (lengthString == null || lengthString.trim().isEmpty()) {
            lengthErrMsg.setValue("Please enter the length of the hike");
            return false;
        }

        double length;
        try {
            length = Double.parseDouble(lengthString);
            if (length <= 0) {
                lengthErrMsg.setValue("Please enter a positive number for length");
                return false;
            }
        } catch (NumberFormatException e) {
            lengthErrMsg.setValue("Please enter a valid number for length");
            return false;
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

        long id = databaseHelper.updateHike(hike);
        return id != -1;
    }
}