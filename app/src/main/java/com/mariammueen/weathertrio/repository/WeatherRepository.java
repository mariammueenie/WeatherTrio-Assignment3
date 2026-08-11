package com.mariammueen.weathertrio.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mariammueen.weathertrio.model.WeatherData;
import com.mariammueen.weathertrio.model.WeatherLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Repository responsible for retrieving weather and city-search data.
 *
 * Keeping the network code here follows the MVVM structure taught in class:
 *
 * View -> ViewModel -> Repository -> Open-Meteo
 *
 * Fragments and Activities should not perform network requests directly.
 */
public class WeatherRepository {

    private static final String TAG = "WeatherRepository";


    /*
        * One OkHttpClient instance is reused for the app's
        * Open-Meteo network requests.
 */
    private final OkHttpClient client = new OkHttpClient();

    /*
     * Keeps track of the current request.
     *
     * Later, WeatherViewModel.onCleared() will use the Repository
     * to cancel a request that is still running.
     */
    private Call currentCall;

    /**
     * Callback used to return either weather data or an error
     * from the asynchronous network request.
     */
    public interface WeatherCallback {

        void onSuccess(WeatherData weatherData);

        void onError(String errorMessage);
    }

    /**
     * Requests current weather information for the selected city.
     */

    /**
         * Callback used for the Assignment 3 city search.
         *
         * The Repository returns either a list of matching cities
         * or a friendly error message.
         */
        public interface LocationSearchCallback {

        void onSuccess(List<WeatherLocation> locations);

        void onError(String errorMessage);
        }


/**
 * Requests current weather from Open-Meteo using the exact
 * latitude and longitude selected from the Search screen.
 *
 * Assignment 3 requires Weather Detail to use the coordinates
 * passed from the selected city instead of searching again by name.
 */
public void getCurrentWeatherByCoordinates(
        String cityName,
        String region,
        String country,
        double latitude,
        double longitude,
        WeatherCallback callback
) {

    Log.d(
            TAG,
            "Requesting Open-Meteo weather for "
                    + cityName
                    + " at "
                    + latitude
                    + ", "
                    + longitude
    );

    /*
     * Build the Open-Meteo Forecast API URL.
     *
     * We only request the current values needed by
     * the existing Weather Detail screen.
     */
    HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host("api.open-meteo.com")
            .addPathSegment("v1")
            .addPathSegment("forecast")
            .addQueryParameter(
                    "latitude",
                    Double.toString(latitude)
            )
            .addQueryParameter(
                    "longitude",
                    Double.toString(longitude)
            )
            .addQueryParameter(
                    "current",
                    "temperature_2m,apparent_temperature,weather_code,wind_speed_10m"
            )
            .build();

    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

    /*
     * Keep the current Call so WeatherViewModel
     * can cancel it if the ViewModel is destroyed.
     */
    currentCall = client.newCall(request);

    /*
     * enqueue() performs the request away from
     * Android's main UI thread.
     */
    currentCall.enqueue(new Callback() {

        @Override
        public void onFailure(
                @NonNull Call call,
                @NonNull IOException exception
        ) {

            if (call.isCanceled()) {

                Log.d(
                        TAG,
                        "Open-Meteo weather request was cancelled"
                );

                return;
            }

            Log.e(
                    TAG,
                    "Open-Meteo weather request failed for "
                            + cityName,
                    exception
            );

            callback.onError(
                    "Unable to load weather. Please check your connection and try again."
            );
        }

        @Override
        public void onResponse(
                @NonNull Call call,
                @NonNull Response response
        ) {

            try (Response responseToClose = response) {

                /*
                 * The server may respond but still return
                 * an unsuccessful HTTP status.
                 */
                if (!responseToClose.isSuccessful()) {

                    Log.w(
                            TAG,
                            "Open-Meteo weather returned HTTP "
                                    + responseToClose.code()
                    );

                    callback.onError(
                            "Weather information could not be loaded."
                    );

                    return;
                }

                if (responseToClose.body() == null) {

                    Log.w(
                            TAG,
                            "Open-Meteo weather returned no response body"
                    );

                    callback.onError(
                            "Weather information could not be loaded."
                    );

                    return;
                }

                String responseBody =
                        responseToClose.body().string();

                /*
                 * Keep using the manual JSONObject parsing
                 * style already used in this project.
                 */
                WeatherData result =
                        parseOpenMeteoWeatherResponse(
                                responseBody,
                                cityName,
                                region,
                                country
                        );

                Log.i(
                        TAG,
                        "Open-Meteo weather loaded for "
                                + cityName
                );

                callback.onSuccess(result);

            } catch (IOException | JSONException exception) {

                Log.e(
                        TAG,
                        "Unable to process Open-Meteo weather response",
                        exception
                );

                callback.onError(
                        "Weather information could not be processed."
                );
            }
        }
    });
}




/**
 * Searches for cities using the Open-Meteo Geocoding API.
 *
 * Assignment 3 requires dynamic city results rather than
 * the hardcoded locations used in Assignment 2.
 */
        public void searchLocations(
                        String searchText,
                        LocationSearchCallback callback
                ) {

                Log.d(TAG, "Searching for locations matching " + searchText);

                /*
                * Build the Open-Meteo geocoding URL.
                *
                * No API key is required for this endpoint.
                */
                HttpUrl url = new HttpUrl.Builder()
                        .scheme("https")
                        .host("geocoding-api.open-meteo.com")
                        .addPathSegment("v1")
                        .addPathSegment("search")
                        .addQueryParameter("name", searchText)
                        .addQueryParameter("count", "10")
                        .addQueryParameter("language", "en")
                        .addQueryParameter("format", "json")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                /*
                * Store the Call so it can be cancelled if the
                * ViewModel is cleared.
                */
                currentCall = client.newCall(request);

                /*
                * enqueue() performs the request asynchronously so
                * the Android UI thread is not blocked.
                */
                currentCall.enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                @NonNull Call call,
                                @NonNull IOException exception
                        ) {

                        if (call.isCanceled()) {
                                Log.d(TAG, "Location search was cancelled");
                                return;
                        }

                        Log.e(
                                TAG,
                                "Location search failed",
                                exception
                        );

                        callback.onError(
                                "Unable to search for cities. Please check your connection and try again."
                        );
                        }

                        @Override
                        public void onResponse(
                                @NonNull Call call,
                                @NonNull Response response
                        ) {

                        try (Response responseToClose = response) {

                                if (!responseToClose.isSuccessful()) {

                                Log.w(
                                        TAG,
                                        "Open-Meteo returned HTTP "
                                                + responseToClose.code()
                                );

                                callback.onError(
                                        "City search could not be completed."
                                );

                                return;
                                }

                                if (responseToClose.body() == null) {

                                callback.onError(
                                        "City search returned no data."
                                );

                                return;
                                }

                                String responseBody =
                                        responseToClose.body().string();

                                List<WeatherLocation> locations =
                                        parseLocationSearchResponse(responseBody);

                                callback.onSuccess(locations);

                        } catch (IOException | JSONException exception) {

                                Log.e(
                                        TAG,
                                        "Unable to process location search response",
                                        exception
                                );

                                callback.onError(
                                        "City search results could not be processed."
                                );
                        }
                        }
                });
                }

/**
 * Converts the Open-Meteo geocoding JSON response
 * into WeatherLocation model objects.
 */
private List<WeatherLocation> parseLocationSearchResponse(
        String responseBody
) throws JSONException {

    List<WeatherLocation> locations =
            new ArrayList<>();

    JSONObject root =
            new JSONObject(responseBody);

    /*
     * Open-Meteo may return no "results" array when
     * no matching city is found.
     */
    JSONArray results =
            root.optJSONArray("results");

    if (results == null) {
        return locations;
    }

    for (int index = 0; index < results.length(); index++) {

        JSONObject result =
                results.getJSONObject(index);

        String cityName =
                result.optString("name", "");

        String region =
                result.optString("admin1", "");

        String country =
                result.optString("country", "");

        double latitude =
                result.optDouble("latitude", 0.0);

        double longitude =
                result.optDouble("longitude", 0.0);

        WeatherLocation location =
                new WeatherLocation(
                        cityName,
                        region,
                        country,
                        latitude,
                        longitude
                );

        locations.add(location);
    }

    return locations;
}


/**
 * Converts the current-weather section returned by Open-Meteo
 * into the existing WeatherData model.
 */
private WeatherData parseOpenMeteoWeatherResponse(
        String responseBody,
        String cityName,
        String region,
        String country
) throws JSONException {

    JSONObject root =
            new JSONObject(responseBody);

    JSONObject current =
            root.getJSONObject("current");

    /*
     * Open-Meteo returns Celsius and km/h by default.
     */
    double temperatureCelsius =
            current.getDouble("temperature_2m");

    double feelsLikeCelsius =
            current.getDouble("apparent_temperature");

    double windKph =
            current.getDouble("wind_speed_10m");

    int weatherCode =
            current.getInt("weather_code");

    /*
     * WeatherData already stores both Celsius and Fahrenheit,
     * so calculate Fahrenheit from the Celsius value.
     */
    double temperatureFahrenheit =
            (temperatureCelsius * 9.0 / 5.0) + 32.0;

    double feelsLikeFahrenheit =
            (feelsLikeCelsius * 9.0 / 5.0) + 32.0;

    /*
     * Keep the same region/country display format
     */
    String fullRegion;

    if (region.isEmpty()) {

        fullRegion = country;

    } else if (country.isEmpty()) {

        fullRegion = region;

    } else {

        fullRegion =
                region + ", " + country;
    }

    String conditionText =
            getWeatherConditionText(weatherCode);

    /*
     * The current UI uses a local weather icon,
     * so no remote icon URL is needed here.
     */
    String conditionIconUrl = "";

    return new WeatherData(
            cityName,
            fullRegion,
            temperatureCelsius,
            temperatureFahrenheit,
            conditionText,
            windKph,
            feelsLikeCelsius,
            feelsLikeFahrenheit,
            conditionIconUrl
    );
}

/**
 * Converts Open-Meteo's WMO weather code
 * into text that can be shown on Weather Detail.
 */
private String getWeatherConditionText(
        int weatherCode
) {

    switch (weatherCode) {

        case 0:
            return "Clear sky";

        case 1:
            return "Mainly clear";

        case 2:
            return "Partly cloudy";

        case 3:
            return "Overcast";

        case 45:
        case 48:
            return "Fog";

        case 51:
        case 53:
        case 55:
            return "Drizzle";

        case 56:
        case 57:
            return "Freezing drizzle";

        case 61:
        case 63:
        case 65:
            return "Rain";

        case 66:
        case 67:
            return "Freezing rain";

        case 71:
        case 73:
        case 75:
            return "Snow";

        case 77:
            return "Snow grains";

        case 80:
        case 81:
        case 82:
            return "Rain showers";

        case 85:
        case 86:
            return "Snow showers";

        case 95:
            return "Thunderstorm";

        case 96:
        case 99:
            return "Thunderstorm with hail";

        default:
            return "Unknown conditions";
    }
}



    /**
     * Cancels a network request that is still running.
     *
     * WeatherViewModel will call this from onCleared()
     * to satisfy the lifecycle requirement in Assignment 2.
     */
    public void cancelPendingRequests() {

        if (currentCall != null && !currentCall.isCanceled()) {

            Log.d(TAG, "Cancelling pending weather request");

            currentCall.cancel();
        }
    }
}