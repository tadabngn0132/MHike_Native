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

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentAddEditBinding;

import java.util.Calendar;

public class AddEditHikeFragment extends Fragment {

    private FragmentAddEditBinding binding;
    private AddEditHikeViewModel addHikeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addHikeViewModel = new ViewModelProvider(this).get(AddEditHikeViewModel.class);

        binding = FragmentAddEditBinding.inflate(inflater, container, false);

        return binding.getRoot();
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

        // Set up the difficulty and parking available spinners
        // Difficulty Spinner
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.difficulty_options, android.R.layout.simple_spinner_item);
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);
        // Parking Available Spinner
        ArrayAdapter<CharSequence> parkingAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.parking_available, android.R.layout.simple_spinner_item);
        binding.spinnerParkingAvailable.setAdapter(parkingAdapter);
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

        // Call ViewModel method to add the hike
        boolean isHikeCreated = addHikeViewModel.addHike(name, location, date, length, difficulty, parkingAvailable, description);

        Toast.makeText(getContext(), isHikeCreated ? "Hike added successfully!" : "Failed to add hike. Please try again.", Toast.LENGTH_SHORT).show();
        onClickedResetBtn();
    }

    private void onClickedResetBtn() {
        binding.editTextHikeName.setText("");
        binding.editTextLocation.setText("");
        binding.editTextDate.setText("");
        binding.editTextLength.setText("");
        binding.spinnerDifficulty.setSelection(0);
        binding.spinnerParkingAvailable.setSelection(0);
        binding.editTextDescription.setText("");
    }
}