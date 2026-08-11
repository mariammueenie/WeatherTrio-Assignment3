package com.mariammueen.weathertrio.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariammueen.weathertrio.databinding.ItemWeatherLocationBinding;
import com.mariammueen.weathertrio.model.WeatherLocation;

import java.util.List;

/**
 * Connects WeatherLocation model objects to the RecyclerView.
 *
 * RecyclerView does not know how to display a WeatherLocation by itself,
 * so this adapter creates each row and places the model data into it.
 */
public class WeatherLocationAdapter
        extends RecyclerView.Adapter<WeatherLocationAdapter.LocationViewHolder> {

    private final List<WeatherLocation> locations;
    private final OnLocationClickListener clickListener;

    /**
     * The Fragment implements this behaviour so it can respond
     * when the user taps one of the city cards.
     */
    public interface OnLocationClickListener {
        void onLocationClick(WeatherLocation location);
    }

    public WeatherLocationAdapter(
            List<WeatherLocation> locations,
            OnLocationClickListener clickListener
    ) {
        this.locations = locations;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        // Inflates one reusable city-card layout using ViewBinding.
        ItemWeatherLocationBinding binding =
                ItemWeatherLocationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LocationViewHolder holder,
            int position
    ) {

        // Gets the correct city for this RecyclerView position.
        WeatherLocation location = locations.get(position);

        holder.bind(location);
    }

   @Override
public int getItemCount() {
    return locations.size();
}

/**
 * Replaces the current RecyclerView data with
 * the newest city-search results.
 */
public void updateLocations(List<WeatherLocation> newLocations) {

    // Remove the previous cities from the adapter's list.
    locations.clear();

    // Add the newest results returned by Open-Meteo.
    locations.addAll(newLocations);

    // Tell RecyclerView to redraw the list with the new data.
    notifyDataSetChanged();
}

/**
 * Holds the views for one RecyclerView row.
 */
class LocationViewHolder extends RecyclerView.ViewHolder {

    private final ItemWeatherLocationBinding binding;

    LocationViewHolder(ItemWeatherLocationBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void bind(WeatherLocation location) {

        // Displays the city name from the location model.
        binding.textCityName.setText(location.getCityName());

        // Assignment 3 requires both the region and country
        // to be shown for each dynamic search result.
        binding.textRegion.setText(
                location.getRegion() + ", " + location.getCountry()
        );

        // Sends the selected WeatherLocation back to SearchFragment
        // when the user taps this RecyclerView item.
        binding.getRoot().setOnClickListener(view ->
                clickListener.onLocationClick(location)
        );
    }
}
}