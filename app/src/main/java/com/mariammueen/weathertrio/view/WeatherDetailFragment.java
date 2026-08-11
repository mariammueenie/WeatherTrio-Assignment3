package com.mariammueen.weathertrio.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mariammueen.weathertrio.databinding.FragmentWeatherDetailBinding;
import com.mariammueen.weathertrio.model.SavedLocation;
import com.mariammueen.weathertrio.model.WeatherData;
import com.mariammueen.weathertrio.preferences.SettingsPreferences;
import com.mariammueen.weathertrio.util.TemperatureFormatter;
import com.mariammueen.weathertrio.viewmodel.SavedViewModel;
import com.mariammueen.weathertrio.viewmodel.WeatherViewModel;

/**
 * Displays live weather information for the city selected
 * from the RecyclerView.
 *
 * This Fragment is part of the View layer.
 * It does not make network requests or access Firestore directly.
 */
public class WeatherDetailFragment extends Fragment {

    // Keys used when Search or Saved passes location information here.
    public static final String ARG_CITY = "city";
    public static final String ARG_REGION = "region";
    public static final String ARG_COUNTRY = "country";
    public static final String ARG_LATITUDE = "latitude";
    public static final String ARG_LONGITUDE = "longitude";

    // ViewBinding gives access to fragment_weather_detail.xml.
    private FragmentWeatherDetailBinding binding;

    // Handles live weather information.
    private WeatherViewModel viewModel;

    // Handles the saved-location Firestore state.
    private SavedViewModel savedViewModel;

    // Reads the temperature unit selected in Settings.
    private SettingsPreferences settingsPreferences;

    // Kotlin helper used to format Celsius or Fahrenheit.
    private TemperatureFormatter temperatureFormatter;

    // Tracks whether the current city is already saved.
private boolean isLocationSaved = false;

    /*
     * Complete location information passed from Search
     * or from the Saved screen.
     */
    private String selectedCity = "";
    private String selectedRegion = "";
    private String selectedCountry = "";

    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // Create the Detail screen using ViewBinding.
        binding = FragmentWeatherDetailBinding.inflate(
                inflater,
                container,
                false
        );

        /*
         * WeatherViewModel keeps weather-network logic
         * outside of this Fragment.
         */
        viewModel = new ViewModelProvider(this)
                .get(WeatherViewModel.class);

        /*
         * SavedViewModel keeps Firestore logic
         * outside of this Fragment.
         */
        savedViewModel = new ViewModelProvider(this)
                .get(SavedViewModel.class);

        /*
         * Read the local Settings preference so the Detail
         * screen knows which temperature unit to display.
         */
        settingsPreferences =
                new SettingsPreferences(requireContext());

        /*
         * TemperatureFormatter is written in Kotlin.
         * Java can use the Kotlin class normally.
         */
        temperatureFormatter =
                new TemperatureFormatter();

        readLocationArguments();
        setupToolbar();
        setupSaveLocationButton();

        observeWeatherViewModel();
        observeSavedViewModel();

        /*
         * Check Firestore when Detail opens so the button
         * can show whether this location is already saved.
         */
        savedViewModel.checkIfLocationSaved(
                createSavedLocation()
        );

        /*
         * Load current weather using the exact coordinates
         * passed from Search or Saved.
         */
        viewModel.loadWeather(
                selectedCity,
                selectedRegion,
                selectedCountry,
                selectedLatitude,
                selectedLongitude
        );

        return binding.getRoot();
    }

    /**
     * Reads the complete location information passed
     * from SearchFragment or SavedFragment.
     */
    private void readLocationArguments() {

        Bundle arguments = getArguments();

        if (arguments != null) {

            selectedCity =
                    arguments.getString(ARG_CITY, "");

            selectedRegion =
                    arguments.getString(ARG_REGION, "");

            selectedCountry =
                    arguments.getString(ARG_COUNTRY, "");

            selectedLatitude =
                    arguments.getDouble(ARG_LATITUDE, 0.0);

            selectedLongitude =
                    arguments.getDouble(ARG_LONGITUDE, 0.0);
        }

        /*
         * Display the location immediately while
         * the weather request is loading.
         */
        binding.textDetailCity.setText(
                selectedCity
        );

        /*
         * Include both region and country when available.
         */
        if (!selectedCountry.isEmpty()) {

            if (!selectedRegion.isEmpty()) {

                binding.textDetailRegion.setText(
                        selectedRegion
                                + ", "
                                + selectedCountry
                );

            } else {

                binding.textDetailRegion.setText(
                        selectedCountry
                );
            }

        } else {

            binding.textDetailRegion.setText(
                    selectedRegion
            );
        }
    }

    /**
     * Configures the toolbar and back button.
     */
    private void setupToolbar() {

        binding.toolbarWeatherDetail.setTitle(
                selectedCity
        );

        binding.toolbarWeatherDetail
                .setNavigationOnClickListener(
                        view ->
                                getParentFragmentManager()
                                        .popBackStack()
                );
    }

    /**
     * Connects the Save Location button to SavedViewModel.
     *
     * The Fragment creates the model, but the ViewModel
     * and Repository perform the Firestore work.
     */
    private void setupSaveLocationButton() {

        binding.buttonSaveLocation.setOnClickListener(
                view -> {

                        SavedLocation location =
                                createSavedLocation();

                        /*
                        * The same button can save or remove
                        * the current city depending on its
                        * Firestore saved state.
                        */
                        if (isLocationSaved) {

                        savedViewModel.removeLocation(
                                location
                        );

                        } else {

                        savedViewModel.saveLocation(
                                location
                        );
                        }
                }
        );
        }

    /**
     * Creates a Firestore-ready SavedLocation from the
     * information passed into this Detail screen.
     */
    private SavedLocation createSavedLocation() {

        return new SavedLocation(
                selectedCity,
                selectedRegion,
                selectedCountry,
                selectedLatitude,
                selectedLongitude
        );
    }

    /**
     * Observes all LiveData exposed by WeatherViewModel.
     */
    private void observeWeatherViewModel() {

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

                        binding.progressWeather.setVisibility(
                                View.VISIBLE
                        );

                        binding.layoutWeatherContent.setVisibility(
                                View.GONE
                        );

                        binding.textWeatherError.setVisibility(
                                View.GONE
                        );

                        binding.buttonRetryWeather.setVisibility(
                                View.GONE
                        );

                    } else {

                        binding.progressWeather.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        /*
         * Display a friendly error if the
         * weather request cannot be completed.
         */
        viewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null
                            && !message.isEmpty()) {

                        binding.layoutWeatherContent.setVisibility(
                                View.GONE
                        );

                        binding.textWeatherError.setText(
                                message
                        );

                        binding.textWeatherError.setVisibility(
                                View.VISIBLE
                        );

                        binding.buttonRetryWeather.setVisibility(
                                View.VISIBLE
                        );
                    }
                }
        );

        /*
         * Retry uses the same exact coordinates that were
         * originally passed to this Detail screen.
         */
        binding.buttonRetryWeather.setOnClickListener(
                view -> viewModel.loadWeather(
                        selectedCity,
                        selectedRegion,
                        selectedCountry,
                        selectedLatitude,
                        selectedLongitude
                )
        );
    }

    /**
     * Observes Firestore saved-location state.
     */
    private void observeSavedViewModel() {

        /*
         * Update the button when Firestore confirms
         * whether this location is already saved.
         */
        savedViewModel.getLocationSaved().observe(
                getViewLifecycleOwner(),
                isSaved -> {

                    /*
                * Remember the current Firestore state so the
                * button knows whether to save or remove.
                */
                isLocationSaved =
                        Boolean.TRUE.equals(isSaved);

                if (isLocationSaved) {

                /*
                * Keep the button enabled so the user
                * can remove this city again.
                */
                binding.buttonSaveLocation.setText(
                        "Remove Location"
                );

                binding.buttonSaveLocation.setEnabled(
                        true
                );

                } else {

                binding.buttonSaveLocation.setText(
                        "Save Location"
                );

                binding.buttonSaveLocation.setEnabled(
                        true
                );
                }
                }
        );

        /*
         * Show success or duplicate-save feedback.
         */
        savedViewModel.getOperationMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null
                            && !message.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        /*
         * Show Firestore errors without crashing
         * or replacing the weather screen.
         */
        savedViewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null
                            && !message.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    /**
     * Displays WeatherData returned through LiveData.
     */
    private void showWeather(
            WeatherData weatherData
    ) {

        if (weatherData == null) {
            return;
        }

        /*
         * Hide loading/error UI and display
         * the weather information.
         */
        binding.progressWeather.setVisibility(
                View.GONE
        );

        binding.textWeatherError.setVisibility(
                View.GONE
        );

        binding.buttonRetryWeather.setVisibility(
                View.GONE
        );

        binding.layoutWeatherContent.setVisibility(
                View.VISIBLE
        );

        binding.textDetailCity.setText(
                weatherData.getCityName()
        );

        binding.textDetailRegion.setText(
                weatherData.getRegion()
        );

        /*
         * Read the temperature unit selected
         * by the user in Settings.
         */
        String selectedUnit =
                settingsPreferences.getTemperatureUnit();

        /*
         * TemperatureFormatter chooses the Celsius
         * or Fahrenheit value based on selectedUnit.
         */
        String temperature =
                temperatureFormatter.formatTemperature(
                        weatherData.getTemperatureCelsius(),
                        weatherData.getTemperatureFahrenheit(),
                        selectedUnit
                );

        binding.textDetailTemperature.setText(
                temperature
        );

        binding.textDetailCondition.setText(
                weatherData.getCondition()
        );

        /*
         * Use the same saved unit for
         * the feels-like temperature.
         */
        String feelsLike =
                temperatureFormatter.formatFeelsLike(
                        weatherData.getFeelsLikeCelsius(),
                        weatherData.getFeelsLikeFahrenheit(),
                        selectedUnit
                );

        binding.textDetailFeelsLike.setText(
                feelsLike
        );

        /*
         * Wind speed remains kilometres per hour.
         */
        String wind =
                String.format(
                        "Wind: %.1f km/h",
                        weatherData.getWindKph()
                );

        binding.textDetailWind.setText(
                wind
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        /*
         * Prevent the Fragment from keeping a reference
         * to a destroyed ViewBinding.
         */
        binding = null;
    }
}