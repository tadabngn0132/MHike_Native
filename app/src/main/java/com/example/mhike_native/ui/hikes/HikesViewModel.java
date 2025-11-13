package com.example.mhike_native.ui.hikes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.DatabaseHelper;

import org.jspecify.annotations.NonNull;

public class HikesViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private DatabaseHelper databaseHelper;

    public HikesViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
        this.mText = new MutableLiveData<>();
        mText.setValue("This is all hikes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    protected void getAllHikes() {
        // Implementation to get all hikes from the database
    }

    protected void updateHike(int hikeId, String name, String location, String dateString, String lengthString, String difficulty, String parkingAvailableString, String description) {
        // Implementation to update a hike by its ID
    }

    protected void deleteHike(int hikeId) {
        // Implementation to delete a hike by its ID
    }
}