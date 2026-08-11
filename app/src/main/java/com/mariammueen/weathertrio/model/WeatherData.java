package com.mariammueen.weathertrio.model;

/**
 * Represents the current weather information returned by OpenMateo.
 *
 * The Repository will create this object after manually parsing
 * the JSON response. The ViewModel will then expose it to the
 * WeatherDetailFragment using LiveData.
 */
public class WeatherData {

    private final String cityName;
    private final String region;
    private final double temperatureCelsius;
    private final double temperatureFahrenheit;
    private final String condition;
    private final double windKph;
    private final double feelsLikeCelsius;
    private final double feelsLikeFahrenheit;
    private final String conditionIconUrl;

    /**
     * Creates one complete current-weather result.
     */
    public WeatherData(
            String cityName,
            String region,
            double temperatureCelsius,
            double temperatureFahrenheit,
            String condition,
            double windKph,
            double feelsLikeCelsius,
            double feelsLikeFahrenheit,
            String conditionIconUrl) {
        this.cityName = cityName;
        this.region = region;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
        this.condition = condition;
        this.windKph = windKph;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.feelsLikeFahrenheit = feelsLikeFahrenheit;
        this.conditionIconUrl = conditionIconUrl;
    }

    public String getCityName() {
        return cityName;
    }

    public String getRegion() {
        return region;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public String getCondition() {
        return condition;
    }

    public double getWindKph() {
        return windKph;
    }

    public double getFeelsLikeCelsius() {
        return feelsLikeCelsius;
    }

    public double getFeelsLikeFahrenheit() {
        return feelsLikeFahrenheit;
    }

    public String getConditionIconUrl() {
        return conditionIconUrl;
    }
}
