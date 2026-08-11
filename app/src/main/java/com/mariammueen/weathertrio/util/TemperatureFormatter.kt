package com.mariammueen.weathertrio.util

import com.mariammueen.weathertrio.preferences.SettingsPreferences

/**
 * Formats weather temperatures using the unit
 * selected by the user in Settings.
 */
class TemperatureFormatter {

    /**
     * Formats the main temperature as either Celsius
     * or Fahrenheit.
     */
    fun formatTemperature(
        celsius: Double,
        fahrenheit: Double,
        unit: String
    ): String {

        return when (unit) {

            SettingsPreferences.UNIT_FAHRENHEIT -> {
                val value = String.format("%.1f", fahrenheit)
                "$value°F"
            }

            else -> {
                val value = String.format("%.1f", celsius)
                "$value°C"
            }
        }
    }

    /**
     * Formats the feels-like temperature using
     * the same selected unit.
     */
    fun formatFeelsLike(
        celsius: Double,
        fahrenheit: Double,
        unit: String
    ): String {

        return when (unit) {

            SettingsPreferences.UNIT_FAHRENHEIT -> {
                val value = String.format("%.1f", fahrenheit)
                "Feels like $value°F"
            }

            else -> {
                val value = String.format("%.1f", celsius)
                "Feels like $value°C"
            }
        }
    }
}