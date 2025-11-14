package com.example.mhike_native.ui.hike_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.helpers.DatabaseHelper;

public class HikeDetailsViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<String> mText;
    public HikeDetailsViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
        this.mText = new MutableLiveData<>();
        mText.setValue("Hike Details");
    }

    public LiveData<String> getText() {
        return mText;
    }

    protected boolean deleteHike(int hikeId) {
        long id = databaseHelper.deleteHike(hikeId);
        return id != -1;
    }
}