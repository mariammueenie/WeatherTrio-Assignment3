package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;
import com.mariammueen.weathertrio.model.WeatherLocation;
import com.mariammueen.weathertrio.view.WeatherLocationAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    // ViewBinding gives access to fragment_search.xml
    // without using findViewById.
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        setupLocationList();

        return binding.getRoot();
    }

    /**
     * Creates the required location models and connects them
     * to the RecyclerView.
     */
    private void setupLocationList() {

        List<WeatherLocation> locations = new ArrayList<>();

        /*
         * Assignment 2 requires Toronto, Montreal,
         * and at least one additional location.
         *
         * The latitude and longitude are stored in the model
         * because the rubric specifically requires them.
         */
        locations.add(
                new WeatherLocation(
                        "Toronto",
                        "Ontario, Canada",
                        43.6532,
                        -79.3832
                )
        );

        locations.add(
                new WeatherLocation(
                        "Montreal",
                        "Quebec, Canada",
                        45.5019,
                        -73.5674
                )
        );

        locations.add(
                new WeatherLocation(
                        "Barrie",
                        "Ontario, Canada",
                        44.3894,
                        -79.6903
                )
        );

        /*
         * LinearLayoutManager displays the RecyclerView
         * as a vertical scrolling list.
         */
        binding.recyclerLocations.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        /*
         * The adapter takes the model list and creates
         * one reusable card for each city.
         */
        WeatherLocationAdapter adapter =
                new WeatherLocationAdapter(
                        locations,
                        this::openWeatherDetail
                );

        binding.recyclerLocations.setAdapter(adapter);
    }

    /**
     * Opens the existing Weather Detail screen when
     * the user selects a city.
     */
    private void openWeatherDetail(WeatherLocation location) {

        Bundle arguments = new Bundle();

        /*
         * For now we only pass location information.
         *
         * Assignment 1 passed fake temperatures and weather.
         * Assignment 2 will fetch those values from WeatherAPI instead.
         */
        arguments.putString(
                WeatherDetailFragment.ARG_CITY,
                location.getCityName()
        );

        arguments.putString(
                WeatherDetailFragment.ARG_REGION,
                location.getRegion()
        );

        /*
         * Store coordinates as well because they are part
         * of the WeatherLocation model required by the rubric.
         */
        arguments.putDouble(
                "latitude",
                location.getLatitude()
        );

        arguments.putDouble(
                "longitude",
                location.getLongitude()
        );

        MainActivity activity =
                (MainActivity) requireActivity();

        activity.openWeatherDetail(arguments);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Prevents the Fragment from holding a destroyed ViewBinding.
        binding = null;
    }
}