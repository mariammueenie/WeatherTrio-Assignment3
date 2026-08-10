package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mariammueen.weathertrio.databinding.FragmentWeatherDetailBinding;
import com.mariammueen.weathertrio.model.WeatherData;
import com.mariammueen.weathertrio.viewmodel.WeatherViewModel;

/**
 * Displays live weather information for the city selected
 * from the RecyclerView.
 *
 * This Fragment is part of the View layer.
 * It does not make network requests or parse JSON.
 */
public class WeatherDetailFragment extends Fragment {

    public static final String ARG_CITY = "city";
    public static final String ARG_REGION = "region";

    private FragmentWeatherDetailBinding binding;
    private WeatherViewModel viewModel;

    private String selectedCity = "";
    private String selectedRegion = "";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentWeatherDetailBinding.inflate(
                inflater,
                container,
                false
        );

        /*
         * ViewModelProvider gives this Fragment its WeatherViewModel.
         * Android keeps the ViewModel separate from the UI itself.
         */
        viewModel = new ViewModelProvider(this)
                .get(WeatherViewModel.class);

        readLocationArguments();
        setupToolbar();
        observeViewModel();

        /*
         * Ask the ViewModel to retrieve the selected city's
         * live weather data.
         */
        viewModel.loadWeather(selectedCity);

        return binding.getRoot();
    }

    /**
     * Reads the city information passed from SearchFragment.
     */
    private void readLocationArguments() {

        Bundle arguments = getArguments();

        if (arguments != null) {

            selectedCity =
                    arguments.getString(ARG_CITY, "");

            selectedRegion =
                    arguments.getString(ARG_REGION, "");
        }

        /*
         * Display the location immediately while the live
         * weather request is loading.
         */
        binding.textDetailCity.setText(selectedCity);
        binding.textDetailRegion.setText(selectedRegion);
    }

    /**
     * Configures the toolbar and back button.
     */
    private void setupToolbar() {

        binding.toolbarWeatherDetail.setTitle(selectedCity);

        binding.toolbarWeatherDetail.setNavigationOnClickListener(
                view -> getParentFragmentManager().popBackStack()
        );
    }

    /**
     * Observes all LiveData exposed by WeatherViewModel.
     *
     * The Fragment only updates the UI when those values change.
     */
    private void observeViewModel() {

        /*
         * Successful weather result.
         */
        viewModel.getWeatherData().observe(
                getViewLifecycleOwner(),
                this::showWeather
        );

        /*
         * Loading state controls the ProgressBar.
         */
        viewModel.getLoading().observe(
                getViewLifecycleOwner(),
                isLoading -> {

                    if (Boolean.TRUE.equals(isLoading)) {

                        binding.progressWeather.setVisibility(View.VISIBLE);
                        binding.layoutWeatherContent.setVisibility(View.GONE);
                        binding.textWeatherError.setVisibility(View.GONE);
                        binding.buttonRetryWeather.setVisibility(View.GONE);

                    } else {

                        binding.progressWeather.setVisibility(View.GONE);
                    }
                }
        );

        /*
         * Error state displays a friendly message instead
         * of allowing a failed request to crash the app.
         */
        viewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null && !message.isEmpty()) {

                        binding.layoutWeatherContent.setVisibility(View.GONE);

                        binding.textWeatherError.setText(message);
                        binding.textWeatherError.setVisibility(View.VISIBLE);

                        binding.buttonRetryWeather.setVisibility(View.VISIBLE);
                    }
                }
        );

        /*
         * Retry simply asks the ViewModel to try the same city again.
         * The Fragment still does not perform any network work itself.
         */
        binding.buttonRetryWeather.setOnClickListener(
                view -> viewModel.loadWeather(selectedCity)
        );
    }

    /**
     * Displays WeatherData returned through LiveData.
     */
    private void showWeather(WeatherData weatherData) {

        if (weatherData == null) {
            return;
        }

        /*
         * Hide loading/error UI and display the weather information.
         */
        binding.progressWeather.setVisibility(View.GONE);
        binding.textWeatherError.setVisibility(View.GONE);
        binding.buttonRetryWeather.setVisibility(View.GONE);
        binding.layoutWeatherContent.setVisibility(View.VISIBLE);

        binding.textDetailCity.setText(
                weatherData.getCityName()
        );

        binding.textDetailRegion.setText(
                weatherData.getRegion()
        );

        /*
         * Assignment 2 requires both Celsius and Fahrenheit.
         */
        String temperature =
                String.format(
                        "%.1f°C  /  %.1f°F",
                        weatherData.getTemperatureCelsius(),
                        weatherData.getTemperatureFahrenheit()
                );

        binding.textDetailTemperature.setText(temperature);

        binding.textDetailCondition.setText(
                weatherData.getCondition()
        );

        /*
         * WeatherAPI calls this feelslike.
         * Display both units just like the main temperature.
         */
        String feelsLike =
                String.format(
                        "Feels like %.1f°C / %.1f°F",
                        weatherData.getFeelsLikeCelsius(),
                        weatherData.getFeelsLikeFahrenheit()
                );

        binding.textDetailFeelsLike.setText(feelsLike);

        String wind =
                String.format(
                        "Wind: %.1f km/h",
                        weatherData.getWindKph()
                );

        binding.textDetailWind.setText(wind);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*
         * Required ViewBinding cleanup.
         * The Fragment must not retain its destroyed View.
         */
        binding = null;
    }
}