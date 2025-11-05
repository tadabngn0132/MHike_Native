package com.example.mhike_native.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddHikeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddHikeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add hike fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}