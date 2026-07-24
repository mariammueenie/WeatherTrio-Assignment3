package com.mariammueen.weathertrio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mariammueen.weathertrio.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    // Gives access to views inside fragment_settings.xml
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Creates Settings screen using ViewBinding
        binding = FragmentSettingsBinding.inflate(
                inflater,
                container,
                false
        );

        // Displays app version from BuildConfig
        // This keeps the version text connected to actual project version
        binding.textAppVersion.setText(
                getString(
                        R.string.settings_version_format,
                        BuildConfig.VERSION_NAME
                )
        );

        // Opens user's email app with feedback information pre-filled
        binding.buttonSendFeedback.setOnClickListener(view -> {
            openFeedbackEmail();
        });

        // Opens selected GitHub or website page in a browser
        binding.buttonViewWebsite.setOnClickListener(view -> {
            openWebsite();
        });

        // Opens Android share sheet with a short app message
        binding.buttonShareApp.setOnClickListener(view -> {
            shareApp();
        });

        return binding.getRoot();
    }

    private void openFeedbackEmail() {
        // ACTION_SENDTO with a mailto URI limits this Intent to email apps
        Uri emailUri = Uri.parse(
                "mailto:mariam_mueen@live.ca"
                        + "?subject="
                        + Uri.encode(getString(R.string.feedback_subject))
        );

        Intent emailIntent = new Intent(
                Intent.ACTION_SENDTO,
                emailUri
        );

        // Checks that email app can handle the Intent
        if (emailIntent.resolveActivity(
                requireContext().getPackageManager()
        ) != null) {
            startActivity(emailIntent);
        } else {
            // Shows clear message instead of allowing the app to crash
            Toast.makeText(
                    requireContext(),
                    R.string.error_no_email_app,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void openWebsite() {
        // TODO: Replace this URL later with the final project repo
        Uri websiteUri = Uri.parse(
                "https://github.com/"
        );

        Intent websiteIntent = new Intent(
                Intent.ACTION_VIEW,
                websiteUri
        );

        // Checks that browser or another compatible app is available
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

    private void shareApp() {
        // ACTION_SEND opens the system share sheet
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // text/plain matches the short text message being shared
        shareIntent.setType("text/plain");

        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_app_message)
        );

        // A chooser lets user select which compatible app to use
        Intent chooserIntent = Intent.createChooser(
                shareIntent,
                getString(R.string.share_app_chooser_title)
        );

        // Checks that at least one app can handle share request
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

        // Clears old view reference when fragment view is destroyed
        binding = null;
    }
}