package com.mariammueen.weathertrio.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariammueen.weathertrio.databinding.ActivityRegisterBinding;

/**
 * Allows a new user to create a WeatherTrio account
 * using Firebase Authentication.
 */
public class RegisterActivity extends AppCompatActivity {

    // ViewBinding gives access to activity_register.xml
    // without using findViewById.
    private ActivityRegisterBinding binding;

    // FirebaseAuth handles account registration.
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gets the Firebase Authentication instance
        // connected to this WeatherTrio Firebase project.
        auth = FirebaseAuth.getInstance();

        // Attempt registration when Register is pressed.
        binding.buttonRegister.setOnClickListener(view ->
                registerUser()
        );

        // Existing users can return to the Login screen.
        binding.textBackToLogin.setOnClickListener(view -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    /**
     * Validates the fields and asks Firebase
     * to create the new email/password account.
     */
    private void registerUser() {

        String email =
                binding.editRegisterEmail.getText().toString().trim();

        String password =
                binding.editRegisterPassword.getText().toString();

        String confirmPassword =
                binding.editConfirmPassword.getText().toString();

        // Hide any previous error before trying again.
        binding.textRegisterError.setVisibility(View.GONE);

        // All registration fields are required.
        if (email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            showError("Please complete all fields.");
            return;
        }

        // Make sure the user typed the same password twice.
        if (!password.equals(confirmPassword)) {

            showError("Passwords do not match.");
            return;
        }

        /*
         * createUserWithEmailAndPassword() is the Firebase
         * registration method taught in class.
         */
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        /*
                         * Firebase automatically signs in the new user
                         * after successful account creation.
                         */
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class
                        );

                        /*
                         * Clear Login/Register screens so the Back button
                         * cannot return to authentication after registration.
                         */
                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);

                    } else {

                        /*
                         * Firebase returns useful messages for problems
                         * such as weak passwords, invalid email addresses,
                         * or an email that is already registered.
                         */
                        if (task.getException() != null) {
                            showError(task.getException().getMessage());
                        } else {
                            showError(
                                    "Unable to create account. Please try again."
                            );
                        }
                    }
                });
    }

    /**
     * Displays a registration error under the form.
     */
    private void showError(String message) {

        binding.textRegisterError.setText(message);
        binding.textRegisterError.setVisibility(View.VISIBLE);
    }
}