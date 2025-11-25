package com.example.mhike_native.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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

public class SearchFragment extends Fragment implements HikeAdapter.OnHikeListener {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        binding.btnClear.setOnClickListener(v -> onClearBtnClicked());
        binding.searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
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