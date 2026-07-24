package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    // Gives access to the views inside fragment_search.xml
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the Search screen layout using ViewBinding
        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        // Opens London's detail screen when its card is selected
        binding.cardLondon.setOnClickListener(view ->
                openWeatherDetail(
                        getString(R.string.city_london),
                        getString(R.string.region_london),
                        "14",
                        "57",
                        "Cloudy",
                        "12",
                        "68",
                        "16"
                )
        );

        // Opens Toronto's detail screen when its card is selected
        binding.cardToronto.setOnClickListener(view ->
                openWeatherDetail(
                        getString(R.string.city_toronto),
                        getString(R.string.region_toronto),
                        "18",
                        "64",
                        "Partly cloudy",
                        "17",
                        "62",
                        "14"
                )
        );

        // Opens Tokyo's detail screen when its card is selected
        binding.cardTokyo.setOnClickListener(view ->
                openWeatherDetail(
                        getString(R.string.city_tokyo),
                        getString(R.string.region_tokyo),
                        "24",
                        "75",
                        "Sunny",
                        "25",
                        "54",
                        "10"
                )
        );

        // Opens Sydney's detail screen when its card is selected
        binding.cardSydney.setOnClickListener(view ->
                openWeatherDetail(
                        getString(R.string.city_sydney),
                        getString(R.string.region_sydney),
                        "21",
                        "70",
                        "Clear",
                        "22",
                        "58",
                        "19"
                )
        );

        // Opens New York's detail screen when its card is selected
        binding.cardNewYork.setOnClickListener(view ->
                openWeatherDetail(
                        getString(R.string.city_new_york),
                        getString(R.string.region_new_york),
                        "16",
                        "61",
                        "Rainy",
                        "15",
                        "73",
                        "22"
                )
        );

        return binding.getRoot();
    }

    private void openWeatherDetail(
            String city,
            String region,
            String temperatureC,
            String temperatureF,
            String condition,
            String feelsLike,
            String humidity,
            String wind
    ) {
        // Creates the separate detail fragment
        WeatherDetailFragment detailFragment =
                new WeatherDetailFragment();

        // Stores the selected city's values in a Bundle
        // Bundles are the required way to pass data between fragments here
        Bundle arguments = new Bundle();
        arguments.putString(
                WeatherDetailFragment.ARG_CITY,
                city
        );
        arguments.putString(
                WeatherDetailFragment.ARG_REGION,
                region
        );
        arguments.putString(
                WeatherDetailFragment.ARG_TEMPERATURE_C,
                temperatureC
        );
        arguments.putString(
                WeatherDetailFragment.ARG_TEMPERATURE_F,
                temperatureF
        );
        arguments.putString(
                WeatherDetailFragment.ARG_CONDITION,
                condition
        );
        arguments.putString(
                WeatherDetailFragment.ARG_FEELS_LIKE,
                feelsLike
        );
        arguments.putString(
                WeatherDetailFragment.ARG_HUMIDITY,
                humidity
        );
        arguments.putString(
                WeatherDetailFragment.ARG_WIND,
                wind
        );

        // Attaches the Bundle to the detail fragment
        detailFragment.setArguments(arguments);

        // Replaces the current content with the detail screen
        // addToBackStack lets the toolbar and system Back return to Search
        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        detailFragment
                )
                .addToBackStack("weather_detail")
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the old view reference when the fragment view is destroyed
        binding = null;
    }
}