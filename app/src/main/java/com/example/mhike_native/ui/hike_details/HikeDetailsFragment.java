package com.example.mhike_native.ui.hike_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentHikeDetailsBinding;

public class HikeDetailsFragment extends Fragment {

    private HikeDetailsViewModel mViewModel;
    private FragmentHikeDetailsBinding binding;

    public static HikeDetailsFragment newInstance() {
        return new HikeDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hike_details, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}