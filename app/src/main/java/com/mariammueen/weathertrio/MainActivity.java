package com.mariammueen.weathertrio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final Fragment citiesFragment = new CitiesFragment();
    private final Fragment detailsFragment = new DetailsFragment();
    private final Fragment settingsFragment = new SettingsFragment();

    private Fragment activeFragment = citiesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, settingsFragment, "settings")
                    .hide(settingsFragment)
                    .add(R.id.fragmentContainer, detailsFragment, "details")
                    .hide(detailsFragment)
                    .add(R.id.fragmentContainer, citiesFragment, "cities")
                    .commit();
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_cities) {
                showFragment(citiesFragment);
                return true;
            }

            if (itemId == R.id.navigation_details) {
                showFragment(detailsFragment);
                return true;
            }

            if (itemId == R.id.navigation_settings) {
                showFragment(settingsFragment);
                return true;
            }

            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();

        activeFragment = fragment;
    }
}