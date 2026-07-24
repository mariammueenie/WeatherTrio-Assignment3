// package com.mariammueen.weathertrio;

// import android.content.Intent;
// import android.net.Uri;
// import android.os.Bundle;
// import android.provider.Settings;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;

// import androidx.annotation.NonNull;
// import androidx.annotation.Nullable;
// import androidx.fragment.app.Fragment;

// import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;

// public class SettingsFragment extends Fragment {

//     // Stores references to the views in fragment_settings.xml
//     // ViewBinding avoids using findViewById for every button
//     private FragmentSettingsBinding binding;

//     @Nullable
//     @Override
//     public View onCreateView(
//             @NonNull LayoutInflater inflater,
//             @Nullable ViewGroup container,
//             @Nullable Bundle savedInstanceState
//     ) {
//         // Creates the fragment layout and connects it to ViewBinding
//         binding = FragmentSettingsBinding.inflate(inflater, container, false);

//         // Opens the device's Wi-Fi settings screen
//         // gives user a quick way to manage their connection
//         binding.buttonWifiSettings.setOnClickListener(view -> {
//             Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//             startActivity(intent);
//         });

//         // Opens devices location settings screen
//         // allows user to turn location services on/off
//         binding.buttonLocationSettings.setOnClickListener(view -> {
//             Intent intent = new Intent(
//                     Settings.ACTION_LOCATION_SOURCE_SETTINGS
//             );

//             startActivity(intent);
//         });

//         // Opens Android settings page for app
//         // users can manage permissions and other app settings
//         binding.buttonAppSettings.setOnClickListener(view -> {
//             Intent intent = new Intent(
//                     Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//             );

//             // Creates link to current WeatherTrio app package
//             // Android uses this to know which app settings page to open
//             Uri packageUri = Uri.parse(
//                     "package:" + requireContext().getPackageName()
//             );

//             intent.setData(packageUri);
//             startActivity(intent);
//         });

//         // Returns completed fragment layout to Android
//         return binding.getRoot();
//     }

//     @Override
//     public void onDestroyView() {
//         super.onDestroyView();

//         // Clears binding when fragment view is destroyed
//         // helps prevent fragment from holding old view references
//         binding = null;
//     }
// }

package com.mariammueen.weathertrio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    // Gives access to the views inside fragment_settings.xml
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates the Settings screen using ViewBinding
        binding = FragmentSettingsBinding.inflate(
                inflater,
                container,
                false
        );

        /*
         * The required settings intents will be added in the next step.
         *
         * This version only confirms that the new layout and button IDs
         * compile correctly before adding the Intent logic.
         */

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clears the old view reference when the fragment view is destroyed
        binding = null;
    }
}