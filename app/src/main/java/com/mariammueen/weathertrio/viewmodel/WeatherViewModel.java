package com.mariammueen.weathertrio.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mariammueen.weathertrio.model.WeatherData;
import com.mariammueen.weathertrio.repository.WeatherRepository;

/**
 * Stores and manages the Weather Detail screen state.
 *
 * The Fragment observes this ViewModel instead of
 * performing Open-Meteo network requests directly.
 */
public class WeatherViewModel extends ViewModel {

    private static final String TAG = "WeatherViewModel";

    /*
     * The Repository performs the actual Open-Meteo
     * weather request.
     */
    private final WeatherRepository repository =
            new WeatherRepository();

    /*
     * MutableLiveData stays private so only the ViewModel
     * can change the weather screen state.
     */
    private final MutableLiveData<WeatherData> weatherData =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> errorMessage =
            new MutableLiveData<>();

    /*
     * Remembers the exact location currently loaded
     * or being loaded.
     *
     * Latitude and longitude are included because different
     * cities can have the same name.
     */
    private String loadedLocationKey = null;

    /**
     * Gives WeatherDetailFragment read-only access
     * to the current WeatherData.
     */
    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    /**
     * Lets the Fragment observe whether a weather
     * request is currently running.
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Lets the Fragment observe weather-loading errors.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Loads current weather for the exact location selected
     * from the Open-Meteo city-search results.
     *
     * Assignment 3 passes latitude and longitude to
     * Weather Detail, so those coordinates are forwarded
     * to WeatherRepository.
     */
    public void loadWeather(
            String cityName,
            String region,
            String country,
            double latitude,
            double longitude
    ) {

        /*
         * Create one identifier for this exact location.
         */
        String locationKey =
                cityName
                        + "|"
                        + latitude
                        + "|"
                        + longitude;

        /*
         * Do not start another request if this exact
         * location is already being loaded.
         */
        if (locationKey.equals(loadedLocationKey)
                && Boolean.TRUE.equals(loading.getValue())) {

            Log.d(
                    TAG,
                    "Weather request already running for "
                            + cityName
            );

            return;
        }

        WeatherData existingWeather =
                weatherData.getValue();

        /*
         * If this exact location has already loaded successfully,
         * reuse its existing LiveData.
         *
         * This helps avoid another API request after something
         * like a screen rotation.
         */
        if (locationKey.equals(loadedLocationKey)
                && existingWeather != null) {

            Log.d(
                    TAG,
                    "Reusing existing weather for "
                            + cityName
            );

            return;
        }

        /*
         * Remember which exact location is now being requested.
         */
        loadedLocationKey = locationKey;

        Log.d(
                TAG,
                "Loading Open-Meteo weather for "
                        + cityName
        );

        /*
         * Clear old WeatherData before requesting a different
         * location.
         *
         * This prevents an older city's weather from being
         * mistaken for the current city if a request fails.
         */
        weatherData.setValue(null);

        /*
         * Tell the Fragment that loading has started.
         */
        loading.setValue(true);

        /*
         * Clear any previous error before another request.
         */
        errorMessage.setValue(null);

        /*
         * Repository performs the actual Open-Meteo request
         * using the selected latitude and longitude.
         */
        repository.getCurrentWeatherByCoordinates(
                cityName,
                region,
                country,
                latitude,
                longitude,
                new WeatherRepository.WeatherCallback() {

                    @Override
                    public void onSuccess(
                            WeatherData result
                    ) {

                        /*
                         * OkHttp callbacks run on a background thread,
                         * so postValue() safely updates LiveData.
                         */
                        weatherData.postValue(result);
                        loading.postValue(false);

                        Log.i(
                                TAG,
                                "Open-Meteo weather successfully loaded"
                        );
                    }

                    @Override
                    public void onError(
                            String message
                    ) {

                        errorMessage.postValue(message);
                        loading.postValue(false);

                        Log.w(
                                TAG,
                                "Open-Meteo weather could not be loaded"
                        );
                    }
                }
        );
    }

    /**
     * Called automatically when Android permanently
     * destroys this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();

        /*
         * Cancel a network request if one is
         * still running.
         */
        repository.cancelPendingRequests();

        Log.d(
                TAG,
                "WeatherViewModel cleared"
        );
    }
}