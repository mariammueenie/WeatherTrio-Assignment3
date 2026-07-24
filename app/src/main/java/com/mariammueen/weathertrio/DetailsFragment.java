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
        selectedCity = city;
        selectedTemperature = temperature;
        selectedCondition = condition;
        selectedHumidity = humidity;
        selectedWind = wind;

        updateDetails();
    }

    private void updateDetails() {
        if (binding == null || selectedCity == null) {
            return;
        }

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
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        updateDetails();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}