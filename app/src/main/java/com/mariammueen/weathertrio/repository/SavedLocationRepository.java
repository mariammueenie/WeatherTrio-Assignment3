package com.mariammueen.weathertrio.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mariammueen.weathertrio.model.SavedLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for saved-location Firestore operations.
 *
 * Keeping Firestore access here follows the MVVM structure:
 *
 * View -> ViewModel -> Repository -> Firestore
 */
public class SavedLocationRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    /**
     * Gets the Firebase services used by this Repository.
     */
    public SavedLocationRepository() {

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Callback used when saving a location.
     */
    public interface SaveLocationCallback {

        void onSaved();

        void onAlreadySaved();

        void onError(String message);
    }

    /**
     * Callback used when removing a saved location.
     */
    public interface RemoveLocationCallback {

        void onRemoved();

        void onError(String message);
    }

    /**
     * Callback used when checking whether a location
     * already exists in Firestore.
     */
    public interface SavedStateCallback {

        void onResult(boolean isSaved);

        void onError(String message);
    }

    /**
     * Callback used by the Saved screen's
     * Firestore snapshot listener.
     */
    public interface SavedLocationsCallback {

        void onLocationsChanged(
                List<SavedLocation> locations
        );

        void onError(String message);
    }

    /**
     * Gets the current user's savedLocations collection.
     *
     * Each Firebase user gets their own collection:
     *
     * users
     *   -> UID
     *      -> savedLocations
     */
    private CollectionReference getSavedLocationsCollection() {

        FirebaseUser currentUser =
                auth.getCurrentUser();

        if (currentUser == null) {
            return null;
        }

        return db.collection("users")
                .document(currentUser.getUid())
                .collection("savedLocations");
    }

    /**
     * Creates a predictable Firestore document ID
     * using the location's coordinates.
     *
     * This helps prevent the same location from
     * being saved more than once.
     */
    private String getLocationDocumentId(
            SavedLocation location
    ) {

        return Double.toString(
                location.getLatitude()
        )
                + "_"
                + Double.toString(
                location.getLongitude()
        );
    }

    /**
     * Saves a location for the currently signed-in user.
     */
    public void saveLocation(
            SavedLocation location,
            SaveLocationCallback callback
    ) {

        CollectionReference collection =
                getSavedLocationsCollection();

        if (collection == null) {

            callback.onError(
                    "You must be signed in to save a location."
            );

            return;
        }

        String documentId =
                getLocationDocumentId(location);

        DocumentReference document =
                collection.document(documentId);

        /*
         * Check for the document first so we can give
         * friendly duplicate-save feedback.
         */
        document.get()
                .addOnSuccessListener(
                        snapshot -> {

                            if (snapshot.exists()) {

                                callback.onAlreadySaved();

                                return;
                            }

                            /*
                             * set() writes the SavedLocation fields
                             * into the predictable document ID.
                             */
                            document.set(location)
                                    .addOnSuccessListener(
                                            unused ->
                                                    callback.onSaved()
                                    )
                                    .addOnFailureListener(
                                            exception ->
                                                    callback.onError(
                                                            "Location could not be saved."
                                                    )
                                    );
                        }
                )
                .addOnFailureListener(
                        exception ->
                                callback.onError(
                                        "Unable to check whether this location is already saved."
                                )
                );
    }

    /**
     * Checks whether the selected location already
     * exists in the user's Firestore collection.
     */
    public void isLocationSaved(
            SavedLocation location,
            SavedStateCallback callback
    ) {

        CollectionReference collection =
                getSavedLocationsCollection();

        if (collection == null) {

            callback.onError(
                    "You must be signed in to check saved locations."
            );

            return;
        }

        String documentId =
                getLocationDocumentId(location);

        collection.document(documentId)
                .get()
                .addOnSuccessListener(
                        snapshot ->
                                callback.onResult(
                                        snapshot.exists()
                                )
                )
                .addOnFailureListener(
                        exception ->
                                callback.onError(
                                        "Unable to check saved location."
                                )
                );
    }

    /**
     * Removes one saved location from Firestore.
     */
    public void removeLocation(
            SavedLocation location,
            RemoveLocationCallback callback
    ) {

        CollectionReference collection =
                getSavedLocationsCollection();

        if (collection == null) {

            callback.onError(
                    "You must be signed in to remove a location."
            );

            return;
        }

        String documentId =
                getLocationDocumentId(location);

        collection.document(documentId)
                .delete()
                .addOnSuccessListener(
                        unused ->
                                callback.onRemoved()
                )
                .addOnFailureListener(
                        exception ->
                                callback.onError(
                                        "Location could not be removed."
                                )
                );
    }

    /**
     * Starts a real-time listener for the current
     * user's saved locations.
     *
     * The listener sends the current documents immediately
     * and sends another update whenever Firestore changes.
     */
    public ListenerRegistration listenForSavedLocations(
            SavedLocationsCallback callback
    ) {

        CollectionReference collection =
                getSavedLocationsCollection();

        if (collection == null) {

            callback.onError(
                    "You must be signed in to view saved locations."
            );

            return null;
        }

        return collection.addSnapshotListener(
                (snapshot, error) -> {

                    if (error != null) {

                        callback.onError(
                                "Saved locations could not be loaded."
                        );

                        return;
                    }

                    List<SavedLocation> locations =
                            new ArrayList<>();

                    if (snapshot != null) {

                        for (QueryDocumentSnapshot document
                                : snapshot) {

                            /*
                             * Firestore converts each document
                             * into our SavedLocation POJO.
                             */
                            SavedLocation location =
                                    document.toObject(
                                            SavedLocation.class
                                    );

                            locations.add(location);
                        }
                    }

                    callback.onLocationsChanged(
                            locations
                    );
                }
        );
    }
}