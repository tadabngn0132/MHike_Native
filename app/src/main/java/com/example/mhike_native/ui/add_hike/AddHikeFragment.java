package com.example.mhike_native.ui.add_hike;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentAddBinding;

public class AddHikeFragment extends Fragment {

    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddHikeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AddHikeViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAdd.setOnClickListener(v -> onClickedAddHikeBtn());
        binding.btnReset.setOnClickListener(v -> onClickedResetBtn());

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.difficulty_options, android.R.layout.simple_spinner_item);
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);
        ArrayAdapter<CharSequence> parkingAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.parking_available, android.R.layout.simple_spinner_item);
        binding.spinnerParkingAvailable.setAdapter(parkingAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickedAddHikeBtn() {
        // Implement the logic to add a hike
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