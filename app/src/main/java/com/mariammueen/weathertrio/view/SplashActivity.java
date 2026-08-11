package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.mariammueen.weathertrio.databinding.ActivitySplashBinding;
import com.mariammueen.weathertrio.preferences.SettingsPreferences;

/**
 * Displays the splash screen and decides where the user
 * should go based on their Firebase Authentication session.
 */
public class SplashActivity extends AppCompatActivity {

    // Keeps the existing splash screen visible for two seconds.
    private static final long SPLASH_DELAY_MILLISECONDS = 2000;

    // ViewBinding gives access to activity_splash.xml.
    private ActivitySplashBinding binding;

    // FirebaseAuth lets us check whether a user is already signed in.
    private FirebaseAuth auth;

    // Handler controls the existing splash-screen delay.
    private Handler splashHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect the Activity to the splash layout first.
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * Load the saved theme preference.
         *
         * SettingsPreferences is written in Kotlin, but Java can
         * use the class normally.
         */
        SettingsPreferences settingsPreferences =
                new SettingsPreferences(this);

        int savedNightMode;

        if (settingsPreferences.isDarkModeEnabled()) {

            savedNightMode =
                    AppCompatDelegate.MODE_NIGHT_YES;

        } else {

            savedNightMode =
                    AppCompatDelegate.MODE_NIGHT_NO;
        }

        /*
         * Apply the theme only when the saved mode differs
         * from the mode currently being used.
         */
        if (AppCompatDelegate.getDefaultNightMode()
                != savedNightMode) {

            AppCompatDelegate.setDefaultNightMode(
                    savedNightMode
            );
        }

        // Get the Firebase Authentication instance for this app.
        auth = FirebaseAuth.getInstance();

        // Create the Handler used for the splash delay.
        splashHandler =
                new Handler(Looper.getMainLooper());

        // Keep the original two-second splash delay.
        splashHandler.postDelayed(() -> {

            Intent intent;

            /*
             * Firebase keeps the user signed in between app launches.
             *
             * If getCurrentUser() returns a user, skip LoginActivity.
             * Otherwise, send the user to the Login screen.
             */
            if (auth.getCurrentUser() != null) {

                intent = new Intent(
                        SplashActivity.this,
                        MainActivity.class
                );

            } else {

                intent = new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );
            }

            startActivity(intent);

            // Prevent Back from returning to the splash screen.
            finish();

        }, SPLASH_DELAY_MILLISECONDS);
    }

    @Override
    protected void onDestroy() {

        /*
         * Remove any unfinished splash callback if Android
         * recreates this Activity while applying a new theme.
         */
        if (splashHandler != null) {
            splashHandler.removeCallbacksAndMessages(null);
        }

        binding = null;

        super.onDestroy();
    }
}
