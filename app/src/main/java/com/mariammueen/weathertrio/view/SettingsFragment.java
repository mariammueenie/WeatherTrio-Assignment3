package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mariammueen.weathertrio.BuildConfig;
import com.mariammueen.weathertrio.R;
import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;
import com.mariammueen.weathertrio.preferences.SettingsPreferences;

/**
 * Displays WeatherTrio settings and account information.
 */
public class SettingsFragment extends Fragment {

    // Gives access to views inside fragment_settings.xml.
    private FragmentSettingsBinding binding;

    // FirebaseAuth gives access to the signed-in user
    // and handles signing the user out.
    private FirebaseAuth auth;

    // Stores the user's temperature and theme preferences.
    private SettingsPreferences settingsPreferences;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // Creates the Settings screen using ViewBinding.
        binding = FragmentSettingsBinding.inflate(
                inflater,
                container,
                false
        );

        // Gets the Firebase Authentication instance.
        auth = FirebaseAuth.getInstance();

        // Opens the SharedPreferences-backed settings helper.
        settingsPreferences =
                new SettingsPreferences(requireContext());

        // Show the email address of the currently signed-in user.
        displaySignedInUser();

        // Load and connect both user preferences.
        setupTemperaturePreference();
        setupThemePreference();

        // Displays app version from BuildConfig.
        binding.textAppVersion.setText(
                getString(
                        R.string.settings_version_format,
                        BuildConfig.VERSION_NAME
                )
        );

        // Opens the user's email app with feedback information pre-filled.
        binding.buttonSendFeedback.setOnClickListener(view ->
                openFeedbackEmail()
        );

        // Opens the selected GitHub or website page.
        binding.buttonViewWebsite.setOnClickListener(view ->
                openWebsite()
        );

        // Opens the Android share sheet.
        binding.buttonShareApp.setOnClickListener(view ->
                shareApp()
        );

        // Signs the Firebase user out and returns to LoginActivity.
        binding.buttonSignOut.setOnClickListener(view ->
                signOutUser()
        );

        return binding.getRoot();
    }

    /**
     * Loads the saved temperature unit and connects
     * the Celsius/Fahrenheit RadioGroup.
     */
    private void setupTemperaturePreference() {

        String savedUnit =
                settingsPreferences.getTemperatureUnit();

        /*
         * Select the radio button that matches the value
         * already stored in SharedPreferences.
         */
        if (SettingsPreferences.UNIT_FAHRENHEIT.equals(
                savedUnit
        )) {

            binding.radioFahrenheit.setChecked(true);

        } else {

            // Celsius is the default unit.
            binding.radioCelsius.setChecked(true);
        }

        /*
         * Save the new preference whenever the user
         * chooses a different temperature unit.
         */
        binding.radioGroupTemperature.setOnCheckedChangeListener(
                (group, checkedId) -> {

                    if (checkedId == R.id.radioFahrenheit) {

                        settingsPreferences.setTemperatureUnit(
                                SettingsPreferences.UNIT_FAHRENHEIT
                        );

                    } else if (checkedId == R.id.radioCelsius) {

                        settingsPreferences.setTemperatureUnit(
                                SettingsPreferences.UNIT_CELSIUS
                        );
                    }
                }
        );
    }

    /**
     * Loads the saved theme preference and connects
     * the Dark Mode switch.
     */
    private void setupThemePreference() {

        /*
         * Set the switch state before adding the listener.
         * This prevents simply opening Settings from
         * accidentally saving or changing the theme.
         */
        binding.switchDarkMode.setChecked(
                settingsPreferences.isDarkModeEnabled()
        );

        binding.switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    // Save the user's choice.
                    settingsPreferences.setDarkModeEnabled(
                            isChecked
                    );

                    int selectedNightMode;

                    if (isChecked) {

                        selectedNightMode =
                                AppCompatDelegate.MODE_NIGHT_YES;

                    } else {

                        selectedNightMode =
                                AppCompatDelegate.MODE_NIGHT_NO;
                    }

                    /*
                     * Changing the default night mode recreates
                     * the Activity so Android can reload the correct
                     * values/ or values-night/ resources.
                     */
                    if (AppCompatDelegate.getDefaultNightMode()
                            != selectedNightMode) {

                        AppCompatDelegate.setDefaultNightMode(
                                selectedNightMode
                        );
                    }
                }
        );
    }

    /**
     * Displays the current Firebase user's email
     * on the Settings screen.
     */
    private void displaySignedInUser() {

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null
                && currentUser.getEmail() != null) {

            binding.textSignedInEmail.setText(
                    currentUser.getEmail()
            );

        } else {

            binding.textSignedInEmail.setText(
                    R.string.settings_no_signed_in_user
            );
        }
    }

    /**
     * Signs the current Firebase user out and
     * returns the app to the Login screen.
     */
    private void signOutUser() {

        // Firebase immediately clears the current auth session.
        auth.signOut();

        Intent intent = new Intent(
                requireContext(),
                LoginActivity.class
        );

        /*
         * Clear the authenticated screens from the back stack.
         * This prevents Back from returning to MainActivity.
         */
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
    }

    /**
     * Opens the user's email app with feedback information.
     */
    private void openFeedbackEmail() {

        // ACTION_SENDTO with a mailto URI limits this Intent to email apps.
        Uri emailUri = Uri.parse(
                "mailto:mariam_mueen@live.ca"
                        + "?subject="
                        + Uri.encode(
                                getString(R.string.feedback_subject)
                        )
        );

        Intent emailIntent = new Intent(
                Intent.ACTION_SENDTO,
                emailUri
        );

        // Check that an email app can handle the Intent.
        if (emailIntent.resolveActivity(
                requireContext().getPackageManager()
        ) != null) {

            startActivity(emailIntent);

        } else {

            Toast.makeText(
                    requireContext(),
                    R.string.error_no_email_app,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * Opens the project website in a browser.
     */
    private void openWebsite() {

        // TODO: Replace this URL later with the final project repository.
        Uri websiteUri = Uri.parse(
                "https://github.com/"
        );

        Intent websiteIntent = new Intent(
                Intent.ACTION_VIEW,
                websiteUri
        );

        // Check that a browser is available.
        if (websiteIntent.resolveActivity(
                requireContext().getPackageManager()
        ) != null) {

            startActivity(websiteIntent);

        } else {

            Toast.makeText(
                    requireContext(),
                    R.string.error_no_browser,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * Opens Android's share sheet with a short
     * message about WeatherTrio.
     */
    private void shareApp() {

        Intent shareIntent =
                new Intent(Intent.ACTION_SEND);

        // The shared content is plain text.
        shareIntent.setType("text/plain");

        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_app_message)
        );

        Intent chooserIntent =
                Intent.createChooser(
                        shareIntent,
                        getString(
                                R.string.share_app_chooser_title
                        )
                );

        // Check that at least one app can handle sharing.
        if (shareIntent.resolveActivity(
                requireContext().getPackageManager()
        ) != null) {

            startActivity(chooserIntent);

        } else {

            Toast.makeText(
                    requireContext(),
                    R.string.error_no_share_app,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        // Clear the old view reference when this Fragment is destroyed.
        binding = null;
    }
}