package com.mariammueen.weathertrio.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mariammueen.weathertrio.databinding.FragmentSavedBinding;
import com.mariammueen.weathertrio.model.SavedLocation;
import com.mariammueen.weathertrio.viewmodel.SavedViewModel;

import java.util.ArrayList;

/**
 * Displays locations saved by the currently signed-in user.
 *
 * Saved locations are loaded from Cloud Firestore through
 * SavedViewModel and SavedLocationRepository.
 */
public class SavedFragment extends Fragment {

    // Gives access to views inside fragment_saved.xml.
    private FragmentSavedBinding binding;

    // Keeps Firestore logic outside of the Fragment.
    private SavedViewModel savedViewModel;

    // Displays saved locations inside the RecyclerView.
    private SavedLocationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // Create the Saved screen using ViewBinding.
        binding = FragmentSavedBinding.inflate(
                inflater,
                container,
                false
        );

        setupRecyclerView();
        setupViewModel();
        observeViewModel();

        /*
         * Starts listening to the current user's
         * saved locations in Firestore.
         */
        savedViewModel.startListeningForSavedLocations();

        return binding.getRoot();
    }

    /**
     * Sets up the RecyclerView and its click actions.
     */
    private void setupRecyclerView() {

        binding.recyclerSavedLocations.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        /*
         * Start with an empty list.
         *
         * The Firestore snapshot listener will provide
         * the real saved locations.
         */
        adapter = new SavedLocationAdapter(
                new ArrayList<>(),
                this::openWeatherDetail,
                this::removeSavedLocation
        );

        binding.recyclerSavedLocations.setAdapter(adapter);
    }

    /**
     * Gets the SavedViewModel used by this Fragment.
     */
    private void setupViewModel() {

        savedViewModel =
                new ViewModelProvider(this)
                        .get(SavedViewModel.class);
    }

    /**
     * Observes saved locations and Firestore messages.
     */
    private void observeViewModel() {

        /*
         * Update the RecyclerView whenever Firestore
         * sends a new list of saved locations.
         */
        savedViewModel.getSavedLocations().observe(
                getViewLifecycleOwner(),
                locations -> {

                    if (locations == null || locations.isEmpty()) {

                        adapter.updateLocations(
                                new ArrayList<>()
                        );

                        // No saved locations: show the empty state.
                        binding.recyclerSavedLocations.setVisibility(
                                View.GONE
                        );

                        binding.layoutSavedEmpty.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        adapter.updateLocations(locations);

                        // Saved locations exist: show the list.
                        binding.recyclerSavedLocations.setVisibility(
                                View.VISIBLE
                        );

                        binding.layoutSavedEmpty.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        /*
         * Show successful actions such as removing
         * a location from Firestore.
         */
        savedViewModel.getOperationMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null && !message.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        /*
         * Show a friendly message if a Firestore
         * operation fails.
         */
        savedViewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null && !message.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    /**
     * Opens Weather Detail when the user taps
     * one of their saved locations.
     */
    private void openWeatherDetail(
            SavedLocation location
    ) {

        Bundle arguments = new Bundle();

        /*
         * Pass the complete saved location so Weather Detail
         * receives the same information as it does from Search.
         */
        arguments.putString(
                WeatherDetailFragment.ARG_CITY,
                location.getCityName()
        );

        arguments.putString(
                WeatherDetailFragment.ARG_REGION,
                location.getRegion()
        );

        arguments.putString(
                WeatherDetailFragment.ARG_COUNTRY,
                location.getCountry()
        );

        arguments.putDouble(
                WeatherDetailFragment.ARG_LATITUDE,
                location.getLatitude()
        );

        arguments.putDouble(
                WeatherDetailFragment.ARG_LONGITUDE,
                location.getLongitude()
        );

        MainActivity activity =
                (MainActivity) requireActivity();

        activity.openWeatherDetail(arguments);
    }

    /**
     * Removes the selected location from Firestore.
     */
    private void removeSavedLocation(
            SavedLocation location
    ) {

        savedViewModel.removeLocation(location);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Prevents the Fragment from retaining its destroyed ViewBinding.
        binding = null;
    }
}