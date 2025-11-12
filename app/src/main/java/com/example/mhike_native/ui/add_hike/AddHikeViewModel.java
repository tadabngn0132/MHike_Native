package com.example.mhike_native.ui.add_hike;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.example.mhike_native.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddHikeViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> errorMessage;

    public AddHikeViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(getApplication());
        this.mText = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        mText.setValue("Add New Hike");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected boolean addHike(String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            errorMessage.setValue("Please enter a hike name");
            return false;
        }

        if (location == null || location.trim().isEmpty()) {
            errorMessage.setValue("Please enter a location");
            return false;
        }

        // Handle date parsing and validation logic
        if (dateString == null || dateString.trim().isEmpty()) {
            errorMessage.setValue("Please enter a date");
            return false;
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            errorMessage.setValue("Please enter date with correct format (DD/MM/YYYY)");
            return false;
        }

        // Handle length parsing and validation logic
        if (lengthString == null || lengthString.trim().isEmpty()) {
            errorMessage.setValue("Please enter the length of the hike");
            return false;
        }

        double length;
        try {
            length = Double.parseDouble(lengthString);
            if (length <= 0) {
                errorMessage.setValue("Please enter a positive number for length");
                return false;
            }
        } catch (NumberFormatException e) {
            errorMessage.setValue("Please enter a valid number for length");
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

        return id > 0;
    }
}