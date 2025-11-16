package com.example.mhike_native.ui.hikes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mhike_native.adapters.HikeAdapter;
import com.example.mhike_native.databinding.FragmentAllHikesBinding;

public class HikesFragment extends Fragment {

    private FragmentAllHikesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HikesViewModel hikesViewModel =
                new ViewModelProvider(this).get(HikesViewModel.class);

        binding = FragmentAllHikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HikeAdapter hikeAdapter = new HikeAdapter();
        binding.recyclerViewHikes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewHikes.setAdapter(hikeAdapter);
        hikesViewModel.getHikes().observe(getViewLifecycleOwner(), hikeAdapter::setHikeList);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}