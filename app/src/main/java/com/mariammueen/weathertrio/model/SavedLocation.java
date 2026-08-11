package com.mariammueen.weathertrio.model;

/**
 * Represents one weather location saved in Cloud Firestore.
 *
 * This class follows the POJO structure required by Firestore:
 * an empty constructor, regular fields, getters, and setters.
 */
public class SavedLocation {

    private String cityName;
    private String region;
    private String country;
    private double latitude;
    private double longitude;

    /**
     * Required empty constructor.
     *
     * Firestore uses this when converting a document
     * back into a SavedLocation object.
     */
    public SavedLocation() {
    }

    /**
     * Creates a complete location before saving it to Firestore.
     */
    public SavedLocation(
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}