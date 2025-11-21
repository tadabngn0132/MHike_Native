package com.example.mhike_native.ui.add_edit_hike;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentAddEditHikeBinding;
import com.example.mhike_native.models.Hike;
import com.example.mhike_native.ui.hikes.HikesViewModel;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddEditHikeFragment extends Fragment {

    private FragmentAddEditHikeBinding binding;
    private AddEditHikeViewModel addHikeViewModel;
    long hikeId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addHikeViewModel = new ViewModelProvider(this).get(AddEditHikeViewModel.class);

        binding = FragmentAddEditHikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        addHikeViewModel.getIsHikeAdded().observe(getViewLifecycleOwner(), isAdded -> {
            HikesViewModel hikesViewModel = new ViewModelProvider(requireActivity()).get(HikesViewModel.class);
            hikesViewModel.loadAllHikes();
            Toast.makeText(getContext(), isAdded ? "Hike added successfully!" : "Failed to add hike. Please try again.", Toast.LENGTH_SHORT).show();
        });
        addHikeViewModel.getIsHikeUpdated().observe(getViewLifecycleOwner(), isUpdated -> {
            HikesViewModel hikesViewModel = new ViewModelProvider(requireActivity()).get(HikesViewModel.class);
            hikesViewModel.loadAllHikes();
            Toast.makeText(getContext(), isUpdated ? "Hike updated successfully!" : "Failed to update hike. Please try again.", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_add_hike_to_navigation_hikes);
        });
        addHikeViewModel.getHikeNameErrMsg().observe(getViewLifecycleOwner(), hikeNameErrMsg -> {
            binding.tvHikeNameErr.setText(hikeNameErrMsg);
        });
        addHikeViewModel.getLocationErrMsg().observe(getViewLifecycleOwner(), locationErrMsg -> {
            binding.tvLocationErr.setText(locationErrMsg);
        });
        addHikeViewModel.getDateErrMsg().observe(getViewLifecycleOwner(), dateErrMsg -> {
            binding.tvDateErr.setText(dateErrMsg);
        });
        addHikeViewModel.getLengthErrMsg().observe(getViewLifecycleOwner(), lengthErrMsg -> {
            binding.tvLengthErr.setText(lengthErrMsg);
        });

        // Set up the difficulty and parking available spinners
        // Difficulty Spinner
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.difficulty_options, android.R.layout.simple_spinner_item);
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);
        // Parking Available Spinner
        ArrayAdapter<CharSequence> parkingAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.parking_available, android.R.layout.simple_spinner_item);
        binding.spinnerParkingAvailable.setAdapter(parkingAdapter);

        hikeId = getArguments() != null ? getArguments().getLong("hikeId", -1) : -1;
        if (hikeId == -1) {
            binding.btnAdd.setText(R.string.btn_add_hike);
            binding.btnReset.setText(R.string.btn_reset);
        } else {
            binding.btnAdd.setText(R.string.btn_update_hike);
            binding.btnReset.setText(R.string.btn_cancel);
            Hike hike = addHikeViewModel.getHikeById(hikeId);

            binding.editTextHikeName.setText(hike.getName());
            binding.editTextLocation.setText(hike.getLocation());
            binding.editTextDate.setText(hike.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            binding.editTextLength.setText(String.valueOf(hike.getLength_km()));
            binding.spinnerDifficulty.setSelection(hike.getDifficulty().equalsIgnoreCase("Easy") ? 0 : hike.getDifficulty().equalsIgnoreCase("Moderate") ? 1 : 2);
            binding.spinnerParkingAvailable.setSelection(hike.isParking_available() ? 0 : 1);
            binding.editTextDescription.setText(hike.getDescription());
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up date picker for the date EditText
        Calendar calendar = Calendar.getInstance();

//        binding.editTextDate.setOnClickListener(v -> {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                requireContext(),
//                (dateView, year, month, dayOfMonth) -> {
//                    calendar.set(Calendar.YEAR, year);
//                    calendar.set(Calendar.MONTH, month);
//                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                    binding.editTextDate.setText(dateFormat.format(calendar.getTime()));
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            );
//            datePickerDialog.show();
//        });

        // Set up button click listeners
        binding.btnAdd.setOnClickListener(v -> onClickedAddHikeBtn());
        binding.btnReset.setOnClickListener(v -> onClickedResetBtn());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickedAddHikeBtn() {
        // Retrieve input values from the form
        String name = binding.editTextHikeName.getText().toString();
        String location = binding.editTextLocation.getText().toString();
        String date = binding.editTextDate.getText().toString();
        String length = binding.editTextLength.getText().toString();

        // Map spinner selections to their corresponding values
        // Difficulty
        String[] difficultyValues = getResources().getStringArray(R.array.difficulty_options);
        String difficulty = difficultyValues[binding.spinnerDifficulty.getSelectedItemPosition()];

        // Parking Available
        String[] parkingValues = getResources().getStringArray(R.array.parking_values);
        String parkingAvailable = parkingValues[binding.spinnerParkingAvailable.getSelectedItemPosition()];

        String description = binding.editTextDescription.getText().toString();

        if (hikeId != -1) {
            // Call ViewModel method to update the hike
            addHikeViewModel.updateHike(hikeId, name, location, date, length, difficulty, parkingAvailable, description);
        } else {
            // Call ViewModel method to add the hike
            addHikeViewModel.addHike(name, location, date, length, difficulty, parkingAvailable, description);
            onClickedResetBtn();
        }
    }

    private void onClickedResetBtn() {
        if (hikeId != -1) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.popBackStack();
            return;
        }
        binding.editTextHikeName.setText("");
        binding.editTextLocation.setText("");
        binding.editTextDate.setText("");
        binding.editTextLength.setText("");
        binding.spinnerDifficulty.setSelection(0);
        binding.spinnerParkingAvailable.setSelection(0);
        binding.editTextDescription.setText("");
    }
}