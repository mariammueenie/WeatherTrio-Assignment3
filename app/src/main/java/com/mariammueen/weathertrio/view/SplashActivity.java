package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariammueen.weathertrio.databinding.ActivitySplashBinding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect the Activity to the splash layout.
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the Firebase Authentication instance for this app.
        auth = FirebaseAuth.getInstance();

        // Keep the original two-second splash delay.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

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
}