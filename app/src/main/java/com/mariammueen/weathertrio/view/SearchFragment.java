package com.mariammueen.weathertrio.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;
import com.mariammueen.weathertrio.model.WeatherLocation;
import com.mariammueen.weathertrio.viewmodel.SearchViewModel;

import java.util.ArrayList;

/**
 * Displays dynamic city-search results from Open-Meteo.
 */
public class SearchFragment extends Fragment {

    // ViewBinding gives access to fragment_search.xml
    // without using findViewById.
    private FragmentSearchBinding binding;

    // ViewModel stores the dynamic search state.
    private SearchViewModel searchViewModel;

    // Adapter stays attached while the city list changes.
    private WeatherLocationAdapter adapter;

    // Handler is used for the required 300 ms search debounce.
    private final Handler searchHandler =
            new Handler(Looper.getMainLooper());

    private Runnable searchRunnable;

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

        setupRecyclerView();
        setupViewModel();
        setupSearchInput();

        return binding.getRoot();
    }

    /**
     * Sets up the RecyclerView that displays
     * dynamic Open-Meteo city-search results.
     */
    private void setupRecyclerView() {

        binding.recyclerLocations.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        /*
         * Assignment 3 starts with an empty list.
         * Results are added after the user searches.
         */
        adapter = new WeatherLocationAdapter(
                new ArrayList<>(),
                this::openWeatherDetail
        );

        binding.recyclerLocations.setAdapter(adapter);
    }

    /**
     * Creates the SearchViewModel and observes
     * search results, loading state, and errors.
     */
    private void setupViewModel() {

        searchViewModel =
                new ViewModelProvider(this)
                        .get(SearchViewModel.class);

        /*
         * Replace the RecyclerView contents whenever
         * the ViewModel receives new search results.
         */
        searchViewModel.getSearchResults().observe(
                getViewLifecycleOwner(),
                locations -> {

                    adapter.updateLocations(locations);

                    if (locations.isEmpty()) {

                        binding.textSearchMessage.setText(
                                "No cities found. Try another search."
                        );

                        binding.textSearchMessage.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        binding.textSearchMessage.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        /*
         * Show the ProgressBar while a search is running.
         */
        searchViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                isLoading -> {

                    if (Boolean.TRUE.equals(isLoading)) {

                        binding.progressSearch.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        binding.progressSearch.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        /*
         * Display a friendly message if the search fails.
         */
        searchViewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null && !message.isEmpty()) {

                        binding.textSearchMessage.setText(message);

                        binding.textSearchMessage.setVisibility(
                                View.VISIBLE
                        );
                    }
                }
        );
    }

    /**
     * Watches the search box and waits 300 ms after
     * the user stops typing before starting a search.
     */
    private void setupSearchInput() {

        binding.editSearchCity.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence text,
                            int start,
                            int count,
                            int after
                    ) {
                        // No action needed before the text changes.
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence text,
                            int start,
                            int before,
                            int count
                    ) {

                        /*
                         * Cancel the previously scheduled search
                         * if the user types another character.
                         */
                        if (searchRunnable != null) {

                            searchHandler.removeCallbacks(
                                    searchRunnable
                            );
                        }

                        String searchText =
                                text.toString().trim();

                        /*
                         * Clear previous results when
                         * the search box becomes empty.
                         */
                        if (searchText.isEmpty()) {

                            adapter.updateLocations(
                                    new ArrayList<>()
                            );

                            binding.progressSearch.setVisibility(
                                    View.GONE
                            );

                            binding.textSearchMessage.setVisibility(
                                    View.GONE
                            );

                            return;
                        }

                        /*
                         * Wait 300 ms before asking the ViewModel
                         * to search Open-Meteo.
                         */
                        searchRunnable = () ->
                                searchViewModel.searchLocations(
                                        searchText
                                );

                        searchHandler.postDelayed(
                                searchRunnable,
                                300
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // No action needed after the text changes.
                    }
                }
        );
    }

    /**
     * Opens Weather Detail when the user selects a city.
     */
    private void openWeatherDetail(WeatherLocation location) {

        Bundle arguments = new Bundle();

        // Pass the complete selected location to Weather Detail.
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

    @Override
    public void onDestroyView() {

        /*
         * Remove a search that is still waiting for
         * its 300 ms delay before destroying the View.
         */
        if (searchRunnable != null) {

            searchHandler.removeCallbacks(
                    searchRunnable
            );
        }

        super.onDestroyView();

        // Prevent the Fragment from retaining a destroyed ViewBinding.
        binding = null;
    }
}