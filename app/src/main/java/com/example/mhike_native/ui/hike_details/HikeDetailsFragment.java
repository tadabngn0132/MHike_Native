package com.example.mhike_native.ui.hike_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentHikeDetailsBinding;

public class HikeDetailsFragment extends Fragment {

    private FragmentHikeDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HikeDetailsViewModel hikeDetailsViewModel = new ViewModelProvider(this).get(HikeDetailsViewModel.class);
        binding = FragmentHikeDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvHikeDetails;
        hikeDetailsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}