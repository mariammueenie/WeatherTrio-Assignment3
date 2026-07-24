package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentCitiesBinding;

public class CitiesFragment extends Fragment {

    private FragmentCitiesBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);

    // Set click listeners for each city card
        // Toronto Card Listener
        binding.cardToronto.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_toronto),
                    getString(R.string.temperature_toronto),
                    getString(R.string.condition_toronto),
                    getString(R.string.humidity_toronto),
                    getString(R.string.wind_toronto)
            );
        });
    
        // Vancouver Card Listener
        binding.cardVancouver.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_vancouver),
                    getString(R.string.temperature_vancouver),
                    getString(R.string.condition_vancouver),
                    getString(R.string.humidity_vancouver),
                    getString(R.string.wind_vancouver)
            );
        });

        // Calgary Card Listener
        binding.cardCalgary.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_calgary),
                    getString(R.string.temperature_calgary),
                    getString(R.string.condition_calgary),
                    getString(R.string.humidity_calgary),
                    getString(R.string.wind_calgary)
            );
        });

        // Ottawa Card Listener
        binding.cardOttawa.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_ottawa),
                    getString(R.string.temperature_ottawa),
                    getString(R.string.condition_ottawa),
                    getString(R.string.humidity_ottawa),
                    getString(R.string.wind_ottawa)
            );
        });

        // Halifax Card Listener
        binding.cardHalifax.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_halifax),
                    getString(R.string.temperature_halifax),
                    getString(R.string.condition_halifax),
                    getString(R.string.humidity_halifax),
                    getString(R.string.wind_halifax)
            );
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}