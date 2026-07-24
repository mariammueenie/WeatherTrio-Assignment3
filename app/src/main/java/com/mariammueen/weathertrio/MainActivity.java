package com.mariammueen.weathertrio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.View;

import com.mariammueen.weathertrio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Tags help Android find same fragments again after recreation
    private static final String TAG_SEARCH = "search";
    private static final String TAG_SAVED = "saved";
    private static final String TAG_SETTINGS = "settings";

    // Saves which bottom navigation tab was selected
    private static final String KEY_SELECTED_TAB = "selected_tab";

    // Gives access to the views in activity_main.xml
    private ActivityMainBinding binding;

    // SearchFragment contains the required city search screen
    private SearchFragment searchFragment;

    // SavedFragment shows required empty-state screen
    private SavedFragment savedFragment;

    // SettingsFragment contains app settings options
    private SettingsFragment settingsFragment;

    // Tracks which fragment is currently visible
    private Fragment activeFragment;

    // Search is default tab when app first opens
    private int selectedTabId = R.id.navigation_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects MainActivity to activity_main.xml using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Watches for changes to the fragment back stack
        // This restores the bottom navigation after leaving Weather Details
        getSupportFragmentManager()
                .addOnBackStackChangedListener(() -> {

                    boolean detailScreenOpen =
                            getSupportFragmentManager()
                                    .getBackStackEntryCount() > 0;

                    binding.bottomNavigation.setVisibility(
                            detailScreenOpen ? View.GONE : View.VISIBLE
                    );
                });

        if (savedInstanceState == null) {
            // Creates each fragment only the first time activity opens
            searchFragment = new SearchFragment();
            savedFragment = new SavedFragment();
            settingsFragment = new SettingsFragment();

            activeFragment = searchFragment;

            // Adds all three fragments once
            // Saved and Settings start hidden so Search appears first
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(
                            R.id.fragmentContainer,
                            settingsFragment,
                            TAG_SETTINGS
                    )
                    .hide(settingsFragment)
                    .add(
                            R.id.fragmentContainer,
                            savedFragment,
                            TAG_SAVED
                    )
                    .hide(savedFragment)
                    .add(
                            R.id.fragmentContainer,
                            searchFragment,
                            TAG_SEARCH
                    )
                    .commit();
        } else {
            // Reuses SearchFragments Android already restored
            searchFragment = (SearchFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_SEARCH);

            savedFragment = (SavedFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_SAVED);

            settingsFragment = (SettingsFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_SETTINGS);

            // Restores previously selected bottom tab
            selectedTabId = savedInstanceState.getInt(
                    KEY_SELECTED_TAB,
                    R.id.navigation_search
            );

            activeFragment = getFragmentForTab(selectedTabId);
        }

        // Switches fragments when user selects bottom tab
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

        // Selects Search on first launch or restores previous tab
        binding.bottomNavigation.setSelectedItemId(selectedTabId);
    }

    public void openWeatherDetail(Bundle arguments) {
        // Creates the separate weather detail screen
        WeatherDetailFragment detailFragment =
                new WeatherDetailFragment();

        // Gives the selected city's Bundle data to the detail fragment
        detailFragment.setArguments(arguments);

        // Hides the bottom navigation while the detail screen is open
        // The detail screen is not one of the three main tabs
        binding.bottomNavigation.setVisibility(View.GONE);

        // Adds the detail fragment on top of the current tab
        // The Search fragment remains underneath instead of being replaced
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.fragmentContainer,
                        detailFragment,
                        "weather_detail"
                )
                .addToBackStack("weather_detail")
                .commit();
    }

    private Fragment getFragmentForTab(int itemId) {
        // Matches each bottom navigation item with fragment
        if (itemId == R.id.navigation_search) {
            return searchFragment;
        }

        if (itemId == R.id.navigation_saved) {
            return savedFragment;
        }

        if (itemId == R.id.navigation_settings) {
            return settingsFragment;
        }

        return null;
    }

    private void showFragment(Fragment fragment) {
        // Avoids unnecessary transaction if the tab is already visible
        if (fragment == activeFragment) {
            return;
        }

        // Hides current fragment and shows the selected one
        // show/hide preserves fragment state between tab switches
        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();

        activeFragment = fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Saves selected tab before Android recreates the activity
        outState.putInt(KEY_SELECTED_TAB, selectedTabId);

        super.onSaveInstanceState(outState);
    }
}