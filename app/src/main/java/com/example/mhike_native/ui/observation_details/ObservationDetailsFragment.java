package com.example.mhike_native.ui.observation_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhike_native.R;

public class ObservationDetailsFragment extends Fragment {

    private ObservationDetailsViewModel mViewModel;

    public static ObservationDetailsFragment newInstance() {
        return new ObservationDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_observation_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObservationDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}