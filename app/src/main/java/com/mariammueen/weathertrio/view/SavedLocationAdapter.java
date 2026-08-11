package com.mariammueen.weathertrio.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariammueen.weathertrio.databinding.ItemSavedLocationBinding;
import com.mariammueen.weathertrio.model.SavedLocation;

import java.util.List;

/**
 * Connects SavedLocation objects to the Saved screen RecyclerView.
 *
 * Each row lets the user either:
 * - tap the card to open Weather Detail
 * - tap Remove to delete the location from Firestore
 */
public class SavedLocationAdapter
        extends RecyclerView.Adapter<SavedLocationAdapter.SavedLocationViewHolder> {

    private final List<SavedLocation> locations;
    private final OnSavedLocationClickListener clickListener;
    private final OnRemoveClickListener removeClickListener;

    /**
     * Sends a card click back to SavedFragment.
     */
    public interface OnSavedLocationClickListener {
        void onLocationClick(SavedLocation location);
    }

    /**
     * Sends a Remove-button click back to SavedFragment.
     */
    public interface OnRemoveClickListener {
        void onRemoveClick(SavedLocation location);
    }

    public SavedLocationAdapter(
            List<SavedLocation> locations,
            OnSavedLocationClickListener clickListener,
            OnRemoveClickListener removeClickListener
    ) {
        this.locations = locations;
        this.clickListener = clickListener;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public SavedLocationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        /*
         * Creates one item_saved_location.xml row
         * using ViewBinding.
         */
        ItemSavedLocationBinding binding =
                ItemSavedLocationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new SavedLocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SavedLocationViewHolder holder,
            int position
    ) {

        // Get the saved city for this RecyclerView position.
        SavedLocation location =
                locations.get(position);

        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    /**
     * Replaces the currently displayed saved locations
     * with the newest Firestore snapshot.
     */
    public void updateLocations(
            List<SavedLocation> newLocations
    ) {

        locations.clear();
        locations.addAll(newLocations);

        /*
         * Tell RecyclerView to redraw using
         * the newest Firestore data.
         */
        notifyDataSetChanged();
    }

    /**
     * Holds the ViewBinding for one saved-location card.
     */
    class SavedLocationViewHolder
            extends RecyclerView.ViewHolder {

        private final ItemSavedLocationBinding binding;

        SavedLocationViewHolder(
                ItemSavedLocationBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Places SavedLocation data into one card
         * and connects its two click actions.
         */
        void bind(SavedLocation location) {

            // Show the city name.
            binding.textSavedCityName.setText(
                    location.getCityName()
            );

            // Assignment 3 requires country on the Saved screen.
            binding.textSavedCountry.setText(
                    location.getCountry()
            );

            /*
             * Tapping the card opens Weather Detail.
             */
            binding.getRoot().setOnClickListener(view ->
                    clickListener.onLocationClick(location)
            );

            /*
             * Tapping Remove sends only the removal action
             * back to SavedFragment.
             */
            binding.buttonRemoveSavedLocation.setOnClickListener(view ->
                    removeClickListener.onRemoveClick(location)
            );
        }
    }
}