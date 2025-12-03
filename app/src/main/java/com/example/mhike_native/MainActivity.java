package com.example.mhike_native;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mhike_native.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set status bar color
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_hikes, R.id.navigation_add_hike, R.id.search_fragment, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                navController.popBackStack(R.id.navigation_home, false);
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (itemId == R.id.navigation_hikes) {
                navController.popBackStack(R.id.navigation_hikes, false);
                navController.navigate(R.id.navigation_hikes);
                return true;
            } else if (itemId == R.id.navigation_add_hike) {
                navController.popBackStack(R.id.navigation_add_hike, false);
                navController.navigate(R.id.navigation_add_hike);
                return true;
            }
            else if (itemId == R.id.search_fragment) {
                navController.popBackStack(R.id.search_fragment, false);
                navController.navigate(R.id.search_fragment);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                navController.popBackStack(R.id.navigation_profile, false);
                navController.navigate(R.id.navigation_profile);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, (appBarConfiguration)) || super.onSupportNavigateUp();
    }
}