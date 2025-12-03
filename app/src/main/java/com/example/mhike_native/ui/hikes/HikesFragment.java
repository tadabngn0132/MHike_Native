package com.example.mhike_native.ui.hikes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mhike_native.R;
import com.example.mhike_native.adapters.HikeAdapter;
import com.example.mhike_native.databinding.FragmentAllHikesBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HikesFragment extends Fragment implements HikeAdapter.OnHikeListener {

    private FragmentAllHikesBinding binding;
    private HikesViewModel hikesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hikesViewModel = new ViewModelProvider(requireActivity()).get(HikesViewModel.class);

        binding = FragmentAllHikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HikeAdapter hikeAdapter = new HikeAdapter();
        hikeAdapter.setOnClickedHikeListener(this);
        binding.recyclerViewHikes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewHikes.setAdapter(hikeAdapter);
        hikesViewModel.getHikes().observe(getViewLifecycleOwner(), hikes -> {
            if (hikes.isEmpty()) {
                binding.tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmptyState.setVisibility(View.GONE);
            }
            hikeAdapter.setHikeList(hikes);
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.hikes_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_all_hikes) {
                    displayDeleteAllConfirmationDialog();
                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onHikeClicked(long hikeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("hikeId", hikeId);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_hikes_to_hikeDetailsFragment, bundle);
    }

    private void displayDeleteAllConfirmationDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        materialAlertDialogBuilder.setTitle("Confirm Deletion");
        materialAlertDialogBuilder.setMessage("Are you sure you want to delete all hikes? This action cannot be undone.");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.setPositiveButton("Delete", (dialog, which) -> deleteAllHikes());
        materialAlertDialogBuilder.show();
    }

    private void deleteAllHikes() {
        hikesViewModel.deleteAllHikes();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}