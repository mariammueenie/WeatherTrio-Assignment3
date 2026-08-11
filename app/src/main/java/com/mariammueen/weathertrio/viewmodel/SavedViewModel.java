package com.mariammueen.weathertrio.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.ListenerRegistration;
import com.mariammueen.weathertrio.model.SavedLocation;
import com.mariammueen.weathertrio.repository.SavedLocationRepository;

import java.util.List;

/**
 * Stores and manages saved-location state.
 *
 * SavedFragment and WeatherDetailFragment can observe
 * this ViewModel instead of accessing Firestore directly.
 */
public class SavedViewModel extends ViewModel {

    private static final String TAG = "SavedViewModel";

    /*
     * Repository performs the actual Firestore work.
     */
    private final SavedLocationRepository repository =
            new SavedLocationRepository();

    /*
     * Keeps the current Firestore snapshot listener so it
     * can be removed when this ViewModel is destroyed.
     */
    private ListenerRegistration listenerRegistration;

    /*
     * MutableLiveData stays private so only this ViewModel
     * can change saved-location state.
     */
    private final MutableLiveData<List<SavedLocation>> savedLocations =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> locationSaved =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> operationMessage =
            new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage =
            new MutableLiveData<>();

    /**
     * Gives the UI read-only access to the saved locations.
     */
    public LiveData<List<SavedLocation>> getSavedLocations() {
        return savedLocations;
    }

    /**
     * Lets the Detail screen observe whether the
     * current location is already saved.
     */
    public LiveData<Boolean> getLocationSaved() {
        return locationSaved;
    }

    /**
     * Provides success or duplicate feedback to the UI.
     */
    public LiveData<String> getOperationMessage() {
        return operationMessage;
    }

    /**
     * Provides Firestore errors to the UI.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Starts listening to the current user's saved locations.
     *
     * Firestore will immediately return the current documents
     * and send another update whenever the collection changes.
     */
    public void startListeningForSavedLocations() {

        /*
         * Remove an older listener before creating another one.
         */
        stopListeningForSavedLocations();

        errorMessage.setValue(null);

        listenerRegistration =
                repository.listenForSavedLocations(
                        new SavedLocationRepository.SavedLocationsCallback() {

                            @Override
                            public void onLocationsChanged(
                                    List<SavedLocation> locations
                            ) {

                                savedLocations.postValue(locations);

                                Log.d(
                                        TAG,
                                        "Saved locations updated: "
                                                + locations.size()
                                );
                            }

                            @Override
                            public void onError(String message) {

                                errorMessage.postValue(message);

                                Log.w(
                                        TAG,
                                        "Unable to load saved locations"
                                );
                            }
                        }
                );
    }

    /**
     * Saves the selected location to Firestore.
     */
    public void saveLocation(SavedLocation location) {

        errorMessage.setValue(null);
        operationMessage.setValue(null);

        repository.saveLocation(
                location,
                new SavedLocationRepository.SaveLocationCallback() {

                    @Override
                    public void onSaved() {

                        locationSaved.postValue(true);

                        operationMessage.postValue(
                                "Location saved."
                        );

                        Log.d(
                                TAG,
                                "Location saved"
                        );
                    }

                    @Override
                    public void onAlreadySaved() {

                        locationSaved.postValue(true);

                        operationMessage.postValue(
                                "This location is already saved."
                        );

                        Log.d(
                                TAG,
                                "Location already saved"
                        );
                    }

                    @Override
                    public void onError(String message) {

                        errorMessage.postValue(message);

                        Log.w(
                                TAG,
                                "Unable to save location"
                        );
                    }
                }
        );
    }

    /**
     * Checks whether the selected Detail-screen
     * location is already saved in Firestore.
     */
    public void checkIfLocationSaved(SavedLocation location) {

        errorMessage.setValue(null);

        repository.isLocationSaved(
                location,
                new SavedLocationRepository.SavedStateCallback() {

                    @Override
                    public void onResult(boolean isSaved) {

                        locationSaved.postValue(isSaved);

                        Log.d(
                                TAG,
                                "Saved state: " + isSaved
                        );
                    }

                    @Override
                    public void onError(String message) {

                        errorMessage.postValue(message);

                        Log.w(
                                TAG,
                                "Unable to check saved state"
                        );
                    }
                }
        );
    }

    /**
     * Removes the selected saved location from Firestore.
     */
    public void removeLocation(SavedLocation location) {

        errorMessage.setValue(null);
        operationMessage.setValue(null);

        repository.removeLocation(
                location,
                new SavedLocationRepository.RemoveLocationCallback() {

                    @Override
                    public void onRemoved() {

                        operationMessage.postValue(
                                "Location removed."
                        );

                        Log.d(
                                TAG,
                                "Location removed"
                        );
                    }

                    @Override
                    public void onError(String message) {

                        errorMessage.postValue(message);

                        Log.w(
                                TAG,
                                "Unable to remove location"
                        );
                    }
                }
        );
    }

    /**
     * Stops the active Firestore snapshot listener.
     */
    private void stopListeningForSavedLocations() {

        if (listenerRegistration != null) {

            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    /**
     * Called when Android permanently destroys this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();

        stopListeningForSavedLocations();

        Log.d(
                TAG,
                "SavedViewModel cleared"
        );
    }
}