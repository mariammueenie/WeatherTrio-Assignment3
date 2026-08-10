package com.mariammueen.weathertrio.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentSavedBinding;

import com.mariammueen.weathertrio.R;
import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;
import com.mariammueen.weathertrio.databinding.FragmentWeatherDetailBinding;
import com.mariammueen.weathertrio.databinding.FragmentSavedBinding;
import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;
import com.mariammueen.weathertrio.databinding.ActivityMainBinding;
import com.mariammueen.weathertrio.databinding.ActivitySplashBinding;

public class SavedFragment extends Fragment {

    // Gives access to views inside fragment_saved.xml
    // ViewBinding avoids using findViewById
    private FragmentSavedBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates Saved screen layout using ViewBinding
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        // Returns completed layout to Android
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears old view reference when fragment view is destroyed
        binding = null;
    }
}