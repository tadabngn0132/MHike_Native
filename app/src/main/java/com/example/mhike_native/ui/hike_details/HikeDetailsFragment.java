package com.example.mhike_native.ui.hike_details;

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
import android.widget.Toast;

import com.example.mhike_native.R;
import com.example.mhike_native.databinding.FragmentHikeDetailsBinding;
import com.example.mhike_native.models.Hike;
import com.example.mhike_native.ui.hikes.HikesViewModel;

public class HikeDetailsFragment extends Fragment {

    private FragmentHikeDetailsBinding binding;
    private HikeDetailsViewModel hikeDetailsViewModel;
    private long hikeId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hikeDetailsViewModel = new ViewModelProvider(this).get(HikeDetailsViewModel.class);
        binding = FragmentHikeDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        hikeId = getArguments() != null ? getArguments().getLong("hikeId", -1) : -1;

        Hike hike = hikeDetailsViewModel.getHikeById(hikeId);
        binding.tvHikeDetailsName.setText(hike.getName());
        binding.tvHikeDetailsLocation.setText(hike.getLocation());
        binding.tvHikeDetailsDate.setText(hike.getDate().toString());
        String lengthString = hike.getLength_km() + " km";
        binding.tvHikeDetailsLength.setText(lengthString);
        binding.tvHikeDetailsDifficulty.setText(hike.getDifficulty());
        String parkingAvailableString = hike.isParking_available() ? "Yes" : "No";
        binding.tvHikeDetailsParkingAvailable.setText(parkingAvailableString);
        binding.tvHikeDetailsDescription.setText(hike.getDescription());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up delete button click listener
        if (hikeId != -1) {
            binding.btnDelete.setOnClickListener(v -> onDeleteButtonClick(hikeId));
            binding.btnUpdate.setOnClickListener(v -> onUpdateButtonClick(hikeId));
        } else {
            binding.btnDelete.setEnabled(false);
            binding.btnUpdate.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onDeleteButtonClick(long hikeId) {
        boolean isHikeDeleted = hikeDetailsViewModel.deleteHike(hikeId);
        if (isHikeDeleted) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            HikesViewModel hikesViewModel = new ViewModelProvider(requireActivity()).get(HikesViewModel.class);
            hikesViewModel.loadAllHikes();
            navController.popBackStack();
        }
        Toast.makeText(getContext(), isHikeDeleted ? "Hike deleted successfully" : "Failed to delete hike", Toast.LENGTH_SHORT).show();
    }

    private void onUpdateButtonClick(long hikeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("hikeId", hikeId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_hikeDetailsFragment_to_navigation_add_hike, bundle);
    }
}