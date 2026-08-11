package com.mariammueen.weathertrio.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Stores the user's WeatherTrio settings locally.
 *
 * SharedPreferences remembers the temperature unit
 * and theme selected by the user.
 */
class SettingsPreferences(context: Context) {

    // Opens one SharedPreferences file for app settings.
    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

    /**
     * Returns true when Dark Mode is selected.
     *
     * Dark Mode is the default because WeatherTrio
     * originally used a dark-only theme.
     */
    fun isDarkModeEnabled(): Boolean {
        return preferences.getBoolean(
            KEY_DARK_MODE,
            true
        )
    }

    /**
     * Saves the user's theme choice.
     */
    fun setDarkModeEnabled(enabled: Boolean) {
        preferences
            .edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }

    /**
     * Returns the saved temperature unit.
     *
     * Celsius is used as the default.
     * The Elvis operator provides a safe fallback.
     */
    fun getTemperatureUnit(): String {
        return preferences.getString(
            KEY_TEMPERATURE_UNIT,
            UNIT_CELSIUS
        ) ?: UNIT_CELSIUS
    }

    /**
     * Saves the user's selected temperature unit.
     */
    fun setTemperatureUnit(unit: String) {
        preferences
            .edit()
            .putString(KEY_TEMPERATURE_UNIT, unit)
            .apply()
    }

    companion object {

        const val UNIT_CELSIUS = "celsius"
        const val UNIT_FAHRENHEIT = "fahrenheit"

        private const val PREFERENCES_NAME =
            "weathertrio_settings"

        private const val KEY_DARK_MODE =
            "dark_mode"

        private const val KEY_TEMPERATURE_UNIT =
            "temperature_unit"
    }
}
