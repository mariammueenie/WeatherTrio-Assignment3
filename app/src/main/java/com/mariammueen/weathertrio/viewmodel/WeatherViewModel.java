package com.mariammueen.weathertrio.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mariammueen.weathertrio.model.WeatherData;
import com.mariammueen.weathertrio.repository.WeatherRepository;

/**
 * Stores and manages the weather-detail screen state.
 *
 * The Fragment observes this ViewModel instead of calling
 * WeatherAPI directly. This keeps networking out of the View layer.
 */
public class WeatherViewModel extends ViewModel {

    private static final String TAG = "WeatherViewModel";

    /*
     * The ViewModel communicates with the Repository,
     * which is responsible for the actual WeatherAPI request.
     */
    private final WeatherRepository repository =
            new WeatherRepository();

    /*
     * MutableLiveData is private because only the ViewModel
     * should be able to change these values.
     */
    private final MutableLiveData<WeatherData> weatherData =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> errorMessage =
            new MutableLiveData<>();

    /*
     * Remembers which city is currently loaded or being loaded.
     *
     * The ViewModel survives a screen rotation, so this lets us
     * avoid requesting the same city's weather again when Android
     * recreates the Fragment view.
     */
    private String loadedCity = null;

    /**
     * Exposes weather information as read-only LiveData.
     *
     * The Fragment can observe this value but cannot change it.
     */
    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    /**
     * Lets the Fragment observe whether an API request
     * is currently running.
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Lets the Fragment observe network or parsing errors.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Starts loading current weather for the selected city.
     *
     * If the Fragment is recreated during a screen rotation,
     * the existing ViewModel is reused. This prevents the same
     * request from being unnecessarily started again.
     */
    public void loadWeather(String cityName) {

        /*
         * Do not start another request if this same city
         * is already being loaded.
         */
        if (cityName.equals(loadedCity)
                && Boolean.TRUE.equals(loading.getValue())) {

            Log.d(
                    TAG,
                    "Weather request already running for " + cityName
            );

            return;
        }

        /*
         * If weather for this city has already been loaded,
         * keep the existing LiveData instead of requesting it again
         * after a configuration change such as screen rotation.
         */
        WeatherData existingWeather =
                weatherData.getValue();

        if (cityName.equals(loadedCity)
                && existingWeather != null) {

            Log.d(
                    TAG,
                    "Reusing existing weather for " + cityName
            );

            return;
        }

        /*
         * Remember which city this ViewModel is now loading.
         */
        loadedCity = cityName;

        Log.d(
                TAG,
                "Loading weather for " + cityName
        );

        /*
         * Tell the View that loading has started.
         * The Fragment observes this and displays the ProgressBar.
         */
        loading.setValue(true);

        /*
         * Clear any previous error before another request begins.
         */
        errorMessage.setValue(null);

        repository.getCurrentWeather(
                cityName,
                new WeatherRepository.WeatherCallback() {

                    @Override
                    public void onSuccess(WeatherData result) {

                        /*
                         * OkHttp callbacks happen on a background thread,
                         * so postValue() safely updates the LiveData.
                         */
                        weatherData.postValue(result);
                        loading.postValue(false);

                        Log.i(
                                TAG,
                                "Weather successfully loaded"
                        );
                    }

                    @Override
                    public void onError(String message) {

                        errorMessage.postValue(message);
                        loading.postValue(false);

                        Log.w(
                                TAG,
                                "Weather could not be loaded"
                        );
                    }
                }
        );
    }

    /**
     * Called automatically when Android permanently destroys
     * this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();

        /*
         * Cancel any network request that is still running.
         */
        repository.cancelPendingRequests();

        Log.d(
                TAG,
                "ViewModel cleared"
        );
    }
}