package com.example.mhike_native.ui.hikes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HikesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HikesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is all hikes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}