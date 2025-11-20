package com.example.mhike_native.ui.add_edit_observation;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentAddEditObservationBinding;

public class AddEditObservationFragment extends Fragment {

    private FragmentAddEditObservationBinding binding;
    private AddEditObservationViewModel addEditObservationViewModel;

    public static AddEditObservationFragment newInstance() {
        return new AddEditObservationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addEditObservationViewModel = new ViewModelProvider(this).get(AddEditObservationViewModel.class);

        binding = FragmentAddEditObservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnAddObservation.setOnClickListener(v -> onClickedAddObservationBtn());
        binding.btnResetObservationForm.setOnClickListener(v -> onClickedResetBtn());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickedAddObservationBtn() {

    }

    private void onClickedResetBtn() {

    }
}