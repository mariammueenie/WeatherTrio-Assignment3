package com.mariammueen.weathertrio.model;

/**
 * Represents one city returned from the location search.
 *
 * Assignment 3 requires each search result to keep the
 * city name, country, region, latitude, and longitude.
 */
public class WeatherLocation {

    private final String cityName;
    private final String region;
    private final String country;
    private final double latitude;
    private final double longitude;

    /**
     * Creates a complete weather location returned
     * from the geocoding search.
     */
    public WeatherLocation(
            String cityName,
            String region,
            String country,
            double latitude,
            double longitude
    ) {
        this.cityName = cityName;
        this.region = region;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the city name.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Returns the province, state, or administrative region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude.
     */
    public double getLongitude() {
        return longitude;
    }
}