package com.example.mhike_native.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<List<Hike>> hikesLiveData;
    private LocalDate parsedDate = null;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = DatabaseHelper.getInstance(application);
        hikesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Hike>> getHikesLiveData(){
        return hikesLiveData;
    }

    private void validateAndParseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            parsedDate = null;
            return;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            parsedDate = LocalDate.parse(date, formatter);
            System.out.println(parsedDate);
        } catch (Exception e) {
            parsedDate = null;
            LocalDate localDate = LocalDate.parse(date);
            long epochDay = localDate.toEpochDay();
        }
    }

    public void searchHikes(String nameKeyWord, String location, String date, Double minLength, Double maxLength, String difficulty, Boolean parkingAvailable) {
        new Thread(() -> {
            validateAndParseDate(date);
            List<Hike> hikes = databaseHelper.searchHikes(nameKeyWord, location, parsedDate, minLength, maxLength, difficulty, parkingAvailable);
            hikesLiveData.postValue(hikes);
        }).start();
    }
}