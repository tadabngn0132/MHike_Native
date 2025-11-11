package com.example.mhike_native.ui.add_hike;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.example.mhike_native.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddHikeViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;

    public AddHikeViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(getApplication());
    }

    private void addHike(String name, String location, String dateString, boolean parkingAvailable, double length, String difficulty, String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        Hike hike = new Hike(name, location, date, parkingAvailable, length, difficulty, description);
        databaseHelper.addHike(hike);
    }
}