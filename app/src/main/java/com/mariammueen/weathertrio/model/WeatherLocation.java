package com.mariammueen.weathertrio.model;

/**
 * Represents one city displayed in the RecyclerView.
 *
 * The assignment requires each location to store its city name,
 * region, latitude, and longitude in a model object instead of
 * hardcoding those values directly inside a layout.
 */
public class WeatherLocation {

    private final String cityName;
    private final String region;
    private final double latitude;
    private final double longitude;

    /**
     * Creates a weather location with all information required
     * by the Assignment 2 location list.
     */
    public WeatherLocation(
            String cityName,
            String region,
            double latitude,
            double longitude
    ) {
        this.cityName = cityName;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the city name displayed in the RecyclerView.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Returns the province, state, or region for the city.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns the latitude stored for the location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude stored for the location.
     */
    public double getLongitude() {
        return longitude;
    }
}

