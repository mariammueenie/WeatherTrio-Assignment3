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

    // Keys used to save and restore selected city's weather data
    private static final String KEY_CITY = "selected_city";
    private static final String KEY_TEMPERATURE = "selected_temperature";
    private static final String KEY_CONDITION = "selected_condition";
    private static final String KEY_HUMIDITY = "selected_humidity";
    private static final String KEY_WIND = "selected_wind";

    // Gives access to views in fragment_details.xml
    private FragmentDetailsBinding binding;

    // Stores currently selected city's weather information
    private String selectedCity;
    private String selectedTemperature;
    private String selectedCondition;
    private String selectedHumidity;
    private String selectedWind;

    public void setCityDetails(
            String city,
            String temperature,
            String condition,
            String humidity,
            String wind
    ) {
        // Saves selected data in fragment
        // lets Details screen display chosen city
        selectedCity = city;
        selectedTemperature = temperature;
        selectedCondition = condition;
        selectedHumidity = humidity;
        selectedWind = wind;

        updateDetails();
    }

    private void updateDetails() {
        // Stops if layout is not ready or no citys been selected
        if (binding == null || selectedCity == null) {
            return;
        }

        // Displays selected city's weather values
        binding.textCityName.setText(selectedCity);
        binding.textTemperature.setText(selectedTemperature);
        binding.textCondition.setText(selectedCondition);
        binding.textHumidity.setText(selectedHumidity);
        binding.textWind.setText(selectedWind);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates layout and connects to ViewBinding
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        if (savedInstanceState != null) {
            // Restores selected city after Android recreates the fragment
            selectedCity = savedInstanceState.getString(KEY_CITY);
            selectedTemperature = savedInstanceState.getString(KEY_TEMPERATURE);
            selectedCondition = savedInstanceState.getString(KEY_CONDITION);
            selectedHumidity = savedInstanceState.getString(KEY_HUMIDITY);
            selectedWind = savedInstanceState.getString(KEY_WIND);
        }

        // Updates screen if city data already exists
        updateDetails();

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves selected city before Android destroys the fragment
        outState.putString(KEY_CITY, selectedCity);
        outState.putString(KEY_TEMPERATURE, selectedTemperature);
        outState.putString(KEY_CONDITION, selectedCondition);
        outState.putString(KEY_HUMIDITY, selectedHumidity);
        outState.putString(KEY_WIND, selectedWind);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears old view reference when fragment view is destroyed
        binding = null;
    }
}