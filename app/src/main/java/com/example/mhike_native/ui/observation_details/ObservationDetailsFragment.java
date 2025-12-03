package com.example.mhike_native.ui.observation_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentObservationDetailsBinding;
import com.example.mhike_native.models.Observation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.format.DateTimeFormatter;

public class ObservationDetailsFragment extends Fragment {

    private FragmentObservationDetailsBinding binding;
    private ObservationDetailsViewModel observationDetailsViewModel;
    private long observationId = -1;
    private long hikeId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        observationDetailsViewModel = new ViewModelProvider(this).get(ObservationDetailsViewModel.class);
        binding = FragmentObservationDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        observationId = getArguments() != null ? getArguments().getLong("observationId", -1) : -1;
        hikeId = getArguments() != null ? getArguments().getLong("hikeId", -1) : -1;

        Observation observation = observationDetailsViewModel.getObservationById(observationId);
        binding.tvObservationDetailsName.setText(observation.getTitle());
        binding.tvObservationDetailsTimestamp.setText(observation.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
        binding.tvObservationDetailsComments.setText(observation.getComments());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up delete button click listener
        if (observationId != -1) {
            binding.btnDeleteObservation.setOnClickListener(v -> showConfirmAlertDialog(observationId));
            binding.btnUpdateObservation.setOnClickListener(v -> onClickedUpdateObservationBtn(observationId, hikeId));
        } else {
            binding.btnDeleteObservation.setEnabled(false);
            binding.btnUpdateObservation.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickedDeleteObservationBtn(long observationId) {
        boolean isDeletedObservation = observationDetailsViewModel.deleteObservation(observationId);
        if (isDeletedObservation) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.popBackStack();
        }
    }

    private void showConfirmAlertDialog(long observationId) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        materialAlertDialogBuilder.setTitle("Confirm Deletion");
        materialAlertDialogBuilder.setMessage("Are you sure you want to delete this observation? This action cannot be undone.");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.setPositiveButton("Delete", (dialog, which) -> {
            onClickedDeleteObservationBtn(observationId);
        });
        materialAlertDialogBuilder.show();
    }

    private void onClickedUpdateObservationBtn(long observationId, long hikeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("observationId", observationId);
        bundle.putLong("hikeId", hikeId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_observationDetailsFragment_to_addEditObservationFragment, bundle);
    }

}