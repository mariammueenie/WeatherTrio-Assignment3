package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentCitiesBinding;

public class CitiesFragment extends Fragment {

    private FragmentCitiesBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);

        binding.cardToronto.setOnClickListener(view -> {
            MainActivity activity = (MainActivity) requireActivity();

            activity.showCityDetails(
                    getString(R.string.city_toronto),
                    getString(R.string.weather_toronto)
            );
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}