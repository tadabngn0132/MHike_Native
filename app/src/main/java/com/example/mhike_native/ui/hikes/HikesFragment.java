package com.example.mhike_native.ui.hikes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mhike_native.R;
import com.example.mhike_native.adapters.HikeAdapter;
import com.example.mhike_native.databinding.FragmentAllHikesBinding;

public class HikesFragment extends Fragment implements HikeAdapter.OnHikeListener {

    private FragmentAllHikesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HikesViewModel hikesViewModel =
                new ViewModelProvider(requireActivity()).get(HikesViewModel.class);

        binding = FragmentAllHikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HikeAdapter hikeAdapter = new HikeAdapter();
        hikeAdapter.setOnClickedHikeListener(this);
        binding.recyclerViewHikes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewHikes.setAdapter(hikeAdapter);
        hikesViewModel.getHikes().observe(getViewLifecycleOwner(), hikeAdapter::setHikeList);
        return root;
    }

    @Override
    public void onHikeClicked(long hikeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("hikeId", hikeId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_hikes_to_hikeDetailsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}