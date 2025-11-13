package com.example.mhike_native.ui.hikes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mhike_native.DatabaseHelper;
import com.example.mhike_native.models.Hike;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class HikesViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<List<Hike>> hikesLiveData;

    public HikesViewModel(@NonNull Application application) {
        super(application);
        this.databaseHelper = new DatabaseHelper(application);
        this.hikesLiveData = new MutableLiveData<>();
        this.mText = new MutableLiveData<>();
        mText.setValue("All hikes");
        loadAllHikes();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Hike>> getHikes() {
        return hikesLiveData;
    }

    private void loadAllHikes() {
        List<Hike> hikeList = databaseHelper.getAllHikes();
        hikesLiveData.setValue(hikeList);
    }
}