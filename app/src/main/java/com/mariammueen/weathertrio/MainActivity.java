package com.mariammueen.weathertrio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Keys used to identify fragments when Android restores activity
    private static final String TAG_CITIES = "cities";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_SETTINGS = "settings";

    // Stores selected bottom navigation item
    // lets app reopen same tab after activity recreation
    private static final String KEY_SELECTED_TAB = "selected_tab";

    // Gives access to views inside activity_main.xml
    private ActivityMainBinding binding;

    private CitiesFragment citiesFragment;
    private DetailsFragment detailsFragment;
    private SettingsFragment settingsFragment;

    // Keeps track of fragment currently visible
    private Fragment activeFragment;

    // Cities is default tab when app first opens
    private int selectedTabId = R.id.navigation_cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects activity to activity_main.xml using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            // Creates new fragments first time the activity opens
            citiesFragment = new CitiesFragment();
            detailsFragment = new DetailsFragment();
            settingsFragment = new SettingsFragment();

            activeFragment = citiesFragment;

            // Adds all three fragments once
            // Details and Settings start hidden while Cities remains visible
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, settingsFragment, TAG_SETTINGS)
                    .hide(settingsFragment)
                    .add(R.id.fragmentContainer, detailsFragment, TAG_DETAILS)
                    .hide(detailsFragment)
                    .add(R.id.fragmentContainer, citiesFragment, TAG_CITIES)
                    .commit();
        } else {
            // Retrieves fragments Android already restored
            // prevents duplicate fragment instances from being created
            citiesFragment = (CitiesFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_CITIES);

            detailsFragment = (DetailsFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_DETAILS);

            settingsFragment = (SettingsFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_SETTINGS);

            // Restores tab that was selected before recreation
            selectedTabId = savedInstanceState.getInt(
                    KEY_SELECTED_TAB,
                    R.id.navigation_cities
            );

            activeFragment = getFragmentForTab(selectedTabId);
        }

        // Changes fragments when bottom navigation item is selected
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Fragment selectedFragment = getFragmentForTab(itemId);

            if (selectedFragment == null) {
                return false;
            }

            selectedTabId = itemId;
            showFragment(selectedFragment);

            return true;
        });

        // Displays saved tab or Cities when app first opens
        binding.bottomNavigation.setSelectedItemId(selectedTabId);
    }

    private Fragment getFragmentForTab(int itemId) {
        // Matches each navigation item with its fragment
        if (itemId == R.id.navigation_cities) {
            return citiesFragment;
        }

        if (itemId == R.id.navigation_details) {
            return detailsFragment;
        }

        if (itemId == R.id.navigation_settings) {
            return settingsFragment;
        }

        return null;
    }

    private void showFragment(Fragment fragment) {
        // Avoids performing unnecessary transaction
        // when selected fragment is already visible
        if (fragment == activeFragment) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();

        activeFragment = fragment;
    }

    public void showCityDetails(
            String city,
            String temperature,
            String condition,
            String humidity,
            String wind
    ) {
        // Sends selected city's information to DetailsFragment
        detailsFragment.setCityDetails(
                city,
                temperature,
                condition,
                humidity,
                wind
        );

        // Selecting Details item also changes the visible fragment
        binding.bottomNavigation.setSelectedItemId(
                R.id.navigation_details
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Saves currently selected tab before Android recreates activity
        outState.putInt(KEY_SELECTED_TAB, selectedTabId);

        super.onSaveInstanceState(outState);
    }
}