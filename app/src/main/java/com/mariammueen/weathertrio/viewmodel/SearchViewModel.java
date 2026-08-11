package com.mariammueen.weathertrio.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mariammueen.weathertrio.model.WeatherLocation;
import com.mariammueen.weathertrio.repository.WeatherRepository;

import java.util.List;

/**
 * Stores and manages the dynamic city-search state.
 *
 * SearchFragment observes this ViewModel instead of
 * performing network requests directly.
 */
public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    /*
     * The Repository performs the actual Open-Meteo
     * geocoding network request.
     */
    private final WeatherRepository repository =
            new WeatherRepository();

    /*
     * MutableLiveData stays private so only the ViewModel
     * can change the search state.
     */
    private final MutableLiveData<List<WeatherLocation>> searchResults =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> errorMessage =
            new MutableLiveData<>();

    /**
     * Gives the Fragment read-only access to
     * the current list of search results.
     */
    public LiveData<List<WeatherLocation>> getSearchResults() {
        return searchResults;
    }

    /**
     * Lets the Fragment observe whether
     * a city search is currently running.
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Lets the Fragment observe search errors.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Searches for cities matching the user's text.
     */
    public void searchLocations(String searchText) {

        /*
         * Do not make an API request for empty text.
         */
        if (searchText == null || searchText.trim().isEmpty()) {
            return;
        }

        String trimmedSearch =
                searchText.trim();

        Log.d(
                TAG,
                "Searching for " + trimmedSearch
        );

        /*
         * Tell the View that loading has started
         * and clear any previous error.
         */
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.searchLocations(
                trimmedSearch,
                new WeatherRepository.LocationSearchCallback() {

                    @Override
                    public void onSuccess(
                            List<WeatherLocation> locations
                    ) {

                        /*
                         * Repository callbacks run on a background thread,
                         * so postValue() safely updates LiveData.
                         */
                        searchResults.postValue(locations);
                        loading.postValue(false);

                        Log.d(
                                TAG,
                                "Location search returned "
                                        + locations.size()
                                        + " results"
                        );
                    }

                    @Override
                    public void onError(String message) {

                        errorMessage.postValue(message);
                        loading.postValue(false);

                        Log.w(
                                TAG,
                                "Location search failed"
                        );
                    }
                }
        );
    }

    /**
     * Called when Android permanently destroys
     * this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();

        /*
         * Cancel a search request if one is still running.
         */
        repository.cancelPendingRequests();

        Log.d(
                TAG,
                "SearchViewModel cleared"
        );
    }
}