package com.example.mhike_native.ui.hike_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mhike_native.helpers.DatabaseHelper;
import com.example.mhike_native.models.Hike;

public class HikeDetailsViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;
    public HikeDetailsViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
    }

    protected boolean deleteHike(long hikeId) {
        long id = databaseHelper.deleteHike(hikeId);
        return id != -1;
    }

    public Hike getHikeById(long hikeId) {
        return databaseHelper.getHikeById(hikeId);
    }
}