package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;

    private String selectedCity;
    private String selectedWeather;

    public void setCityDetails(String city, String weather) {
        selectedCity = city;
        selectedWeather = weather;

        if (binding != null) {
            binding.textCityName.setText(city);
            binding.textWeatherSummary.setText(weather);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        if (selectedCity != null && selectedWeather != null) {
            binding.textCityName.setText(selectedCity);
            binding.textWeatherSummary.setText(selectedWeather);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}