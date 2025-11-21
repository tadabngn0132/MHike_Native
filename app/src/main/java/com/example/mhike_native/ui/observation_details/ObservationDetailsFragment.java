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

public class ObservationDetailsFragment extends Fragment {

    private FragmentObservationDetailsBinding binding;
    private ObservationDetailsViewModel observationDetailsViewModel;
    private long observationId = -1;

    public static ObservationDetailsFragment newInstance() {
        return new ObservationDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        observationDetailsViewModel = new ViewModelProvider(this).get(ObservationDetailsViewModel.class);
        binding = FragmentObservationDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        observationId = getArguments() != null ? getArguments().getLong("observationId", -1) : -1;

        Observation observation = observationDetailsViewModel.getObservationById(observationId);
        binding.tvObservationDetailsName.setText(observation.getTitle());
        binding.tvObservationDetailsTimestamp.setText(observation.getTimestamp().toString());
        binding.tvObservationDetailsComments.setText(observation.getComments());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up delete button click listener
        if (observationId != -1) {
            binding.btnDeleteObservation.setOnClickListener(v -> onClickedDeleteObservationBtn());
            binding.btnUpdateObservation.setOnClickListener(v -> onClickedUpdateObservationBtn());
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

    private void onClickedDeleteObservationBtn() {
        boolean isDeletedObservation = observationDetailsViewModel.deleteObservation(observationId);
        if (isDeletedObservation) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.popBackStack();
        }
    }

    private void onClickedUpdateObservationBtn() {
        Bundle bundle = new Bundle();
        bundle.putLong("observationId", observationId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_observationDetailsFragment_to_addEditObservationFragment, bundle);
    }

}