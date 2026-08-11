package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariammueen.weathertrio.databinding.ActivityLoginBinding;

/**
 * Allows an existing user to sign in with
 * Firebase Authentication using email and password.
 */
public class LoginActivity extends AppCompatActivity {

    // ViewBinding gives access to activity_login.xml
    // without using findViewById.
    private ActivityLoginBinding binding;

    // FirebaseAuth handles login and user-session information.
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gets the Firebase Authentication instance
        // connected through google-services.json.
        auth = FirebaseAuth.getInstance();

        // Attempt login when the user presses the Login button.
        binding.buttonLogin.setOnClickListener(view ->
                loginUser()
        );

        // Opens the registration screen for users
        // who do not already have an account.
        binding.textCreateAccount.setOnClickListener(view -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    /**
     * Reads the email and password fields and asks
     * Firebase Authentication to sign the user in.
     */
    private void loginUser() {

        String email =
                binding.editLoginEmail.getText().toString().trim();

        String password =
                binding.editLoginPassword.getText().toString();

        // Hide any previous error before trying again.
        binding.textLoginError.setVisibility(View.GONE);

        // Both values are required before Firebase can attempt login.
        if (email.isEmpty() || password.isEmpty()) {

            showError("Please enter both your email and password.");
            return;
        }

        /*
         * signInWithEmailAndPassword() is the Firebase method
         * taught in class for logging in an existing user.
         */
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        // Login worked, so open the main WeatherTrio app.
                        Intent intent = new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );

                        /*
                         * Clears the login screen from the back stack
                         * so Back does not return to Login after signing in.
                         */
                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);

                    } else {

                        // Firebase provides the reason the login failed.
                        if (task.getException() != null) {
                            showError(task.getException().getMessage());
                        } else {
                            showError("Unable to sign in. Please try again.");
                        }
                    }
                });
    }

    /**
     * Displays a friendly authentication error
     * underneath the login controls.
     */
    private void showError(String message) {

        binding.textLoginError.setText(message);
        binding.textLoginError.setVisibility(View.VISIBLE);
    }
}