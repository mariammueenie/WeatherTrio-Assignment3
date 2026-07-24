package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    // Gives access to the views inside fragment_search.xml
    // ViewBinding avoids using findViewById
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the Search screen layout using ViewBinding
        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        /*
         * The city-card click listeners will be added again later.
         *
         * They will open a separate WeatherDetailFragment and pass
         * the selected city through a Bundle, as required.
         */

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the old layout reference when the view is destroyed
        binding = null;
    }
}