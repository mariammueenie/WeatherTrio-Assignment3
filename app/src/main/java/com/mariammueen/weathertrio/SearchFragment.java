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
    // ViewBinding avoids using findViewById
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the Search screen layout
        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        // Opens London's hardcoded weather details
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

        // Opens Toronto's hardcoded weather details
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

        // Opens Tokyo's hardcoded weather details
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

        // Opens Sydney's hardcoded weather details
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

        // Opens New York's hardcoded weather details
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
        // Stores the selected city's information in a Bundle
        // WeatherDetailFragment reads these values when it opens
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

        // MainActivity opens the detail fragment over the Search screen
        // This preserves the Search, Saved, and Settings fragments underneath
        MainActivity activity = (MainActivity) requireActivity();
        activity.openWeatherDetail(arguments);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the old layout reference when the view is destroyed
        binding = null;
    }
}