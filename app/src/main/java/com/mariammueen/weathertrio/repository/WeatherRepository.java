package com.mariammueen.weathertrio.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mariammueen.weathertrio.model.WeatherData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Repository responsible for retrieving current weather data.
 *
 * Keeping the network code here follows the MVVM structure taught in class:
 *
 * View -> ViewModel -> Repository -> WeatherAPI
 *
 * Fragments and Activities should not perform network requests directly.
 */
public class WeatherRepository {

    private static final String TAG = "WeatherRepository";

    /*
     * Replace this placeholder with your real WeatherAPI key when testing.
     *
     * Do not commit your real API key to GitHub.
     */
    private static final String API_KEY =
            "PASTE_YOUR_WEATHER_API_KEY_HERE";

    /*
     * Assignment 2 requires one OkHttpClient instance in the Repository
     * that is reused for all weather requests.
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
    public void getCurrentWeather(
            String cityName,
            WeatherCallback callback
    ) {

        Log.d(TAG, "Requesting current weather for " + cityName);

        /*
         * Build the WeatherAPI URL manually using HttpUrl.Builder,
         * as required by Assignment 2.
         *
         * Result:
         * https://api.weatherapi.com/v1/current.json
         *      ?key=...
         *      &q=Toronto
         *      &aqi=no
         */
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.weatherapi.com")
                .addPathSegment("v1")
                .addPathSegment("current.json")
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", cityName)
                .addQueryParameter("aqi", "no")
                .build();

        /*
         * A Request tells OkHttp which URL should be requested.
         */
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        /*
         * Store the Call so it can be cancelled if the ViewModel
         * is destroyed before the request finishes.
         */
        currentCall = client.newCall(request);

        /*
         * enqueue() runs asynchronously.
         *
         * This prevents network work from blocking Android's main/UI thread.
         */
        currentCall.enqueue(new Callback() {

            /**
             * Called when the request cannot reach the server.
             *
             * Examples:
             * - Wi-Fi is disabled
             * - no Internet connection
             * - connection times out
             */
            @Override
            public void onFailure(
                    @NonNull Call call,
                    @NonNull IOException exception
            ) {

                if (call.isCanceled()) {
                    Log.d(TAG, "Weather request was cancelled");
                    return;
                }

                Log.e(
                        TAG,
                        "Weather request failed for " + cityName,
                        exception
                );

                callback.onError(
                        "Unable to load weather. Please check your connection and try again."
                );
            }

            /**
             * Called when WeatherAPI returns an HTTP response.
             */
            @Override
            public void onResponse(
                    @NonNull Call call,
                    @NonNull Response response
            ) {

                try (Response responseToClose = response) {

                    /*
                     * A response can reach the server but still fail,
                     * for example because of an invalid API key.
                     */
                    if (!responseToClose.isSuccessful()) {

                        Log.w(
                                TAG,
                                "WeatherAPI returned HTTP "
                                        + responseToClose.code()
                        );

                        callback.onError(
                                "Weather information could not be loaded."
                        );

                        return;
                    }

                    if (responseToClose.body() == null) {

                        Log.w(TAG, "WeatherAPI returned an empty response");

                        callback.onError(
                                "Weather information could not be loaded."
                        );

                        return;
                    }

                    String responseBody =
                            responseToClose.body().string();

                    /*
                     * Assignment 2 specifically requires manual
                     * JSON parsing using org.json.JSONObject.
                     */
                    WeatherData weatherData =
                            parseWeatherResponse(responseBody);

                    Log.i(
                            TAG,
                            "Weather data loaded for " + cityName
                    );

                    callback.onSuccess(weatherData);

                } catch (IOException | JSONException exception) {

                    Log.e(
                            TAG,
                            "Unable to process WeatherAPI response",
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
     * Manually parses the JSON returned from WeatherAPI.
     *
     * The fields correspond to the values required on the
     * Assignment 2 weather detail screen.
     */
    private WeatherData parseWeatherResponse(String responseBody)
            throws JSONException {

        JSONObject root =
                new JSONObject(responseBody);

        JSONObject location =
                root.getJSONObject("location");

        JSONObject current =
                root.getJSONObject("current");

        JSONObject condition =
                current.getJSONObject("condition");

        String cityName =
                location.getString("name");

        String region =
                location.getString("region");

        String country =
                location.getString("country");

        /*
         * WeatherAPI separates region and country.
         * Combine them for a clearer display.
         */
        String fullRegion;

        if (region.isEmpty()) {
            fullRegion = country;
        } else {
            fullRegion = region + ", " + country;
        }

        double temperatureCelsius =
                current.getDouble("temp_c");

        double temperatureFahrenheit =
                current.getDouble("temp_f");

        String conditionText =
                condition.getString("text");

        double windKph =
                current.getDouble("wind_kph");

        double feelsLikeCelsius =
                current.getDouble("feelslike_c");

        double feelsLikeFahrenheit =
                current.getDouble("feelslike_f");

        String conditionIconUrl =
                condition.getString("icon");

        /*
         * Convert the parsed JSON fields into our WeatherData model.
         */
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