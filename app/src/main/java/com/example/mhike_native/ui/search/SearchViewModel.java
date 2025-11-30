package com.example.mhike_native.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<List<Hike>> hikesLiveData;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = DatabaseHelper.getInstance(application);
        hikesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Hike>> getHikesLiveData(){
        return hikesLiveData;
    }

    public void searchHikes(String nameKeyWord, String location, String date, Double minLength, Double maxLength, String difficulty, Boolean parkingAvailable) {
        new Thread(() -> {
            List<Hike> hikes = databaseHelper.searchHikes(nameKeyWord, location, date, minLength, maxLength, difficulty, parkingAvailable);
            hikesLiveData.postValue(hikes);
        }).start();
    }
}