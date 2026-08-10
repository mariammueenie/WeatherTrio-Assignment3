package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.mariammueen.weathertrio.databinding.ActivitySplashBinding;

import com.mariammueen.weathertrio.R;
import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;
import com.mariammueen.weathertrio.databinding.FragmentWeatherDetailBinding;
import com.mariammueen.weathertrio.databinding.FragmentSavedBinding;
import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;
import com.mariammueen.weathertrio.databinding.ActivityMainBinding;
import com.mariammueen.weathertrio.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    // Keeps splash screen visible for two seconds
    // short delay before opening MainActivity
    private static final long SPLASH_DELAY_MILLISECONDS = 2000;

    // Gives access to views in activity_splash.xml
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects activity to splash screen layout using ViewBinding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Runs navigation code after the two-second splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Creates Intent that opens main app screen
            Intent intent = new Intent(
                    SplashActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            // Closes SplashActivity after MainActivity opens
            // prevents Back button from returning to splash screen
            finish();

        }, SPLASH_DELAY_MILLISECONDS);
    }
}