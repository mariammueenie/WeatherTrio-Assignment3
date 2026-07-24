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

    // Gives access to the views inside fragment_cities.xml
    // This fragment temporarily acts as the Search screen
    private FragmentCitiesBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the current city-list layout using ViewBinding
        binding = FragmentCitiesBinding.inflate(
                inflater,
                container,
                false
        );

        /*
         * The previous card listeners were removed temporarily.
         *
         * They depended on Details being a bottom-navigation tab,
         * which does not match the assignment requirements.
         *
         * will add new listeners later that open a separate
         * WeatherDetailFragment and pass the selected city in a Bundle.
         */

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the binding when this fragment's view is destroyed
        binding = null;
    }
}