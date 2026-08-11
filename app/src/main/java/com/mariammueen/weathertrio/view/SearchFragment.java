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

public class SearchFragment extends Fragment {

    // ViewBinding gives access to fragment_search.xml
    // without using findViewById.
    private FragmentSearchBinding binding;

    // ViewModel stores the dynamic search state.
        private SearchViewModel searchViewModel;

        // The adapter stays attached to the RecyclerView
        // while its city list changes.
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
     * Creates the required location models and connects them
     * to the RecyclerView.
     */
   /**
 * Sets up the RecyclerView that will display
 * dynamic Open-Meteo city-search results.
 */
        private void setupRecyclerView() {

        binding.recyclerLocations.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        /*
        * Assignment 3 starts with an empty list.
        *
        * Results will be added after the user searches
        * instead of hardcoding Toronto, Montreal, and Barrie.
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
        * Whenever the ViewModel receives new cities,
        * replace the RecyclerView's current list.
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
        * Show the ProgressBar only while an API
        * search is currently running.
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
        * Display a friendly message if the
        * search request fails.
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
                     * If the search box becomes empty,
                     * clear the previous results.
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

        /*
        * Remove a search that is still waiting for
        * its 300 ms delay before destroying the View.
        */
        if (searchRunnable != null) {
                searchHandler.removeCallbacks(searchRunnable);
        }

        super.onDestroyView();

        // Prevents the Fragment from holding a destroyed ViewBinding.
        binding = null;
        }
}