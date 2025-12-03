package com.example.mhike_native.ui.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mhike_native.R;
import com.example.mhike_native.adapters.HikeAdapter;
import com.example.mhike_native.databinding.FragmentSearchBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchFragment extends Fragment implements HikeAdapter.OnHikeListener {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });


        HikeAdapter hikeAdapter = new HikeAdapter();
        hikeAdapter.setOnClickedHikeListener(this);
        binding.recyclerViewSearchHikes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewSearchHikes.setAdapter(hikeAdapter);
        searchViewModel.getHikesLiveData().observe(getViewLifecycleOwner(), hikeAdapter::setHikeList);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar calendar = Calendar.getInstance();

        binding.editTextSearchDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (dateView, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        binding.editTextSearchDate.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        binding.searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch();
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newKeyWord) {
                handleSearch();
                return true;
            }
        });
        binding.btnFilter.setOnClickListener(v -> {
            if (binding.cardViewFilter.getVisibility() == View.GONE) {
                binding.cardViewFilter.setVisibility(View.VISIBLE);
            } else {
                binding.cardViewFilter.setVisibility(View.GONE);
            }
        });
        binding.btnApply.setOnClickListener(v -> handleSearch());
        binding.btnClear.setOnClickListener(v -> onClearBtnClicked());
    }

    private void handleSearch() {
        String nameKeyWord = binding.searchView.getQuery().toString();
        String location = binding.editTextSearchLocation.getText().toString();
        String date = binding.editTextSearchDate.getText().toString();
        Double minLength = null;
        Double maxLength = null;
        String difficulty = null;
        Boolean parkingAvailable = null;

        String minLengthStr = binding.editTextSearchMinLength.getText().toString();
        if (!minLengthStr.isEmpty()) {
            minLength = Double.parseDouble(minLengthStr);
        }

        String maxLengthStr = binding.editTextSearchMaxLength.getText().toString();
        if (!maxLengthStr.isEmpty()) {
            maxLength = Double.parseDouble(maxLengthStr);
        }

        int selectedDifficultyId = binding.rdgDifficulty.getCheckedRadioButtonId();
        if (selectedDifficultyId != -1) {
            TextView selectedDifficultyBtn = binding.rdgDifficulty.findViewById(selectedDifficultyId);
            difficulty = selectedDifficultyBtn.getText().toString();
        }

        int selectedParkingId = binding.rdgParkingAvailable.getCheckedRadioButtonId();
        if (selectedParkingId != -1) {
            TextView selectedParkingBtn = binding.rdgParkingAvailable.findViewById(selectedParkingId);
            parkingAvailable = selectedParkingBtn.getText().toString().equals("Yes");
        }

        searchViewModel.searchHikes(nameKeyWord, location, date, minLength, maxLength, difficulty, parkingAvailable);
    }

    @Override
    public void onHikeClicked(long hikeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("hikeId", hikeId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_hikes_to_hikeDetailsFragment, bundle);
    }

    private void onClearBtnClicked() {
        binding.editTextSearchLocation.setText("");
        binding.editTextSearchDate.setText("");
        binding.editTextSearchMinLength.setText("");
        binding.editTextSearchMaxLength.setText("");
        binding.rdgDifficulty.clearCheck();
        binding.rdgParkingAvailable.clearCheck();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}