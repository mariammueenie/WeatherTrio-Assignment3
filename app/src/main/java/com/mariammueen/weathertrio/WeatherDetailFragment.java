package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentWeatherDetailBinding;

public class WeatherDetailFragment extends Fragment {

    // Bundle keys identify each weather value passed from SearchFragment
    public static final String ARG_CITY = "city";
    public static final String ARG_REGION = "region";
    public static final String ARG_TEMPERATURE_C = "temperature_c";
    public static final String ARG_TEMPERATURE_F = "temperature_f";
    public static final String ARG_CONDITION = "condition";
    public static final String ARG_FEELS_LIKE = "feels_like";
    public static final String ARG_HUMIDITY = "humidity";
    public static final String ARG_WIND = "wind";

    // Gives access to the views inside fragment_weather_detail.xml
    private FragmentWeatherDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the detail-screen layout using ViewBinding
        binding = FragmentWeatherDetailBinding.inflate(
                inflater,
                container,
                false
        );

        // Reads and displays the city information passed in the Bundle
        displayWeatherDetails();

        // Returns to Search by removing this fragment from the back stack
        binding.toolbarWeatherDetail.setNavigationOnClickListener(view ->
                getParentFragmentManager().popBackStack()
        );

        return binding.getRoot();
    }

    private void displayWeatherDetails() {
        Bundle arguments = getArguments();

        // Stops safely if the fragment was opened without city data
        if (arguments == null) {
            return;
        }

        // Reads each hardcoded value from the fragment Bundle
        String city = arguments.getString(ARG_CITY, "");
        String region = arguments.getString(ARG_REGION, "");
        String temperatureC = arguments.getString(ARG_TEMPERATURE_C, "");
        String temperatureF = arguments.getString(ARG_TEMPERATURE_F, "");
        String condition = arguments.getString(ARG_CONDITION, "");
        String feelsLike = arguments.getString(ARG_FEELS_LIKE, "");
        String humidity = arguments.getString(ARG_HUMIDITY, "");
        String wind = arguments.getString(ARG_WIND, "");

        // Displays the city and region
        binding.textDetailCity.setText(city);
        binding.textDetailRegion.setText(region);

        // Combines Celsius and Fahrenheit into one readable line
        binding.textDetailTemperature.setText(
                getString(
                        R.string.detail_temperature_format,
                        temperatureC,
                        temperatureF
                )
        );

        // Displays the weather condition and remaining measurements
        binding.textDetailCondition.setText(condition);

        binding.textDetailFeelsLike.setText(
                getString(R.string.detail_feels_like_format, feelsLike)
        );

        binding.textDetailHumidity.setText(
                getString(R.string.detail_humidity_format, humidity)
        );

        binding.textDetailWind.setText(
                getString(R.string.detail_wind_format, wind)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the view reference when the fragment layout is destroyed
        binding = null;
    }
}