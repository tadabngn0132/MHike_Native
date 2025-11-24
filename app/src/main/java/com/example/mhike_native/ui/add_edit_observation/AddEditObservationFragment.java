package com.example.mhike_native.ui.add_edit_observation;

import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentAddEditObservationBinding;
import com.example.mhike_native.models.Hike;
import com.example.mhike_native.models.Observation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddEditObservationFragment extends Fragment {

    private FragmentAddEditObservationBinding binding;
    private AddEditObservationViewModel addEditObservationViewModel;
    private long observationId;
    private long hikeId;
    private Hike hike;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addEditObservationViewModel = new ViewModelProvider(this).get(AddEditObservationViewModel.class);

        binding = FragmentAddEditObservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        observationId = getArguments() != null ? getArguments().getLong("observationId", -1) : -1;
        hikeId = getArguments() != null ? getArguments().getLong("hikeId", -1) : -1;

        addEditObservationViewModel.getHikeNameAndDateByHikeId(hikeId);

        addEditObservationViewModel.getHikeLiveData().observe(getViewLifecycleOwner(), hikeWithNameAndDate -> {
            if (hikeWithNameAndDate != null) {
                this.hike = hikeWithNameAndDate;
                binding.tvObservationHikeName.setText(hikeWithNameAndDate.getName());
            }
        });

        if (observationId == -1) {
            requireActivity().setTitle(R.string.label_title_add_observation);
            binding.btnAddObservation.setText(R.string.btn_add_observation);
            binding.btnResetObservationForm.setText(R.string.btn_reset);
        } else {
            requireActivity().setTitle(R.string.label_title_edit_observation);
            binding.btnAddObservation.setText(R.string.btn_update_hike);
            binding.btnResetObservationForm.setText(R.string.btn_cancel);

            addEditObservationViewModel.getObservationById(observationId);

            addEditObservationViewModel.getObservationLiveData().observe(getViewLifecycleOwner(), observation -> {
                if (observation != null) {
                    binding.editTextObservationName.setText(observation.getTitle());
                    String mergedTimestamp = observation.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH));
                    binding.editTextObservationTimestamp.setText(mergedTimestamp.substring(11));
                    binding.editTextComments.setText(observation.getComments());
                }
            });
        }

        addEditObservationViewModel.getIsObservationAdded().observe(getViewLifecycleOwner(), isAdded -> {
            Toast.makeText(getContext(), isAdded ? "Observation added successfully!" : "Failed to add observation. Please try again.", Toast.LENGTH_SHORT).show();
        });

        addEditObservationViewModel.getIsObservationUpdated().observe(getViewLifecycleOwner(), isUpdated -> {
            Bundle bundle = new Bundle();
            bundle.putLong("hikeId", hikeId);
            Toast.makeText(getContext(), isUpdated ? "Observation updated successfully!" : "Failed to update observation. Please try again.", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_addEditObservationFragment_to_hikeDetailsFragment, bundle);
        });

        addEditObservationViewModel.getObservationNameErrMsg().observe(getViewLifecycleOwner(), observationNameErrMsg -> binding.tvObservationNameErr.setText(observationNameErrMsg));

        addEditObservationViewModel.getTimestampErrMsg().observe(getViewLifecycleOwner(), timestampErrMsg -> binding.tvTimestampErr.setText(timestampErrMsg));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar calendar = Calendar.getInstance();

        binding.editTextObservationTimestamp.setOnClickListener(v -> {
            TimePickerDialog datePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (timeView, hourOfDay, minute) -> {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        binding.editTextObservationTimestamp.setText(timeFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
            );
            datePickerDialog.show();
        });

        binding.btnAddObservation.setOnClickListener(v -> onClickedAddObservationBtn());
        binding.btnResetObservationForm.setOnClickListener(v -> onClickedResetBtn());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickedAddObservationBtn() {
        String name = binding .editTextObservationName.getText().toString();
        String timestamp = binding.editTextObservationTimestamp.getText().toString();
        String comments = binding.editTextComments.getText().toString();

        if (hike != null) {
            if (observationId != -1) {
                addEditObservationViewModel.updateObservation(observationId, name, hike.getDate().toString(), timestamp, comments, hikeId);
            } else {
                addEditObservationViewModel.addObservation(name, hike.getDate().toString(), timestamp, comments, hikeId);
                onClickedResetBtn();
            }
        }
    }

    private void onClickedResetBtn() {
        if (observationId != -1) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.popBackStack();
            return;
        }

        binding.editTextObservationName.setText("");
        binding.editTextObservationTimestamp.setText("");
        binding.editTextComments.setText("");
    }
}