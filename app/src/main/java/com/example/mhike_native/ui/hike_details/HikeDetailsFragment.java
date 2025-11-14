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
import android.widget.Toast;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentHikeDetailsBinding;

public class HikeDetailsFragment extends Fragment {

    private FragmentHikeDetailsBinding binding;
    private HikeDetailsViewModel hikeDetailsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hikeDetailsViewModel = new ViewModelProvider(this).get(HikeDetailsViewModel.class);
        binding = FragmentHikeDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvHikeDetails;
        hikeDetailsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up delete button click listener
        int hikeId = getArguments() != null ? getArguments().getInt("hikeId", -1) : -1;
        if (hikeId != -1) {
            binding.deleteButton.setOnClickListener(v -> onDeleteButtonClick(hikeId));
        } else {
            binding.deleteButton.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onDeleteButtonClick(int hikeId) {
        boolean isHikeDeleted = hikeDetailsViewModel.deleteHike(hikeId);
        Toast.makeText(getContext(), isHikeDeleted ? "Hike deleted successfully" : "Failed to delete hike", Toast.LENGTH_SHORT).show();
    }
}