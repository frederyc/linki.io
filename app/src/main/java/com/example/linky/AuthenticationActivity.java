package com.example.linky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.example.linky.backend.services.AuthService;
import com.example.linky.databinding.ActivityAuthenticationBinding;
import com.example.linky.ui.LoadingScreen;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authService = AuthService.getInstance();

        super.onCreate(savedInstanceState);

        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.LRegister.setOnClickListener(view -> handleNavigateToRegister());

        binding.RLogin.setOnClickListener(view -> handleNavigateToLogin());

        binding.RRegister.setOnClickListener(view -> handleRegister(this));

        binding.LLogin.setOnClickListener(view -> handleLoginWithEmailAndPassword(this));
    }

    private void handleNavigateToRegister() {
        binding.loginLayout.setVisibility(View.GONE);
        binding.registerLayout.setVisibility(View.VISIBLE);
    }

    private void handleNavigateToLogin() {
        binding.loginLayout.setVisibility(View.VISIBLE);
        binding.registerLayout.setVisibility(View.GONE);
    }

    private void handleLoginWithEmailAndPassword(Activity activity) {
        LoadingScreen loadingScreen = new LoadingScreen(activity, R.layout.loading_screen);
        loadingScreen.start();

        String email = binding.LEmail.getText().toString();
        String password = binding.LPassword.getText().toString();

        authService.signInWithEmailAndPassword(email, password, () -> {
            clearFields();
            loadingScreen.stop();
            Toast.makeText(getBaseContext(), String.format("%s logged in", email), Toast.LENGTH_SHORT).show();
            Intent navIntent = new Intent(this, MainActivity.class);
            navIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(navIntent);
        }, () -> {
            loadingScreen.stop();
            Toast.makeText(getBaseContext(), "Failed to sign in", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleRegister(Activity activity) {
        LoadingScreen loadingScreen = new LoadingScreen(activity, R.layout.loading_screen);
        loadingScreen.start();

        String fullName = binding.RFullName.getText().toString();
        String email = binding.REmail.getText().toString();
        String password = binding.RPassword.getText().toString();
        String cpassword = binding.RCPassword.getText().toString();

        if (!checkIfNameIsValid(fullName)) {
            binding.RFullName.setError("Name must have at least 6 letters");
            loadingScreen.stop();
            return;
        }

        if (!checkIfEmailAddressIsValid(email)) {
            binding.REmail.setError("Invalid email address");
            loadingScreen.stop();
            return;
        }
        if (!checkIfPasswordIsValid(password)) {
            binding.RPassword.setError("Invalid password");
            loadingScreen.stop();
            return;
        }
        if (!password.equals(cpassword)) {
            binding.RCPassword.setError("Invalid password");
            loadingScreen.stop();
            return;
        }

        authService.createAccount(fullName, email, password, () -> {
            clearFields();
            loadingScreen.stop();
            Toast.makeText(getBaseContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
            Intent navIntent = new Intent(this, MainActivity.class);
            navIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(navIntent);
        }, () -> {
            loadingScreen.stop();
            Toast.makeText(getBaseContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkIfNameIsValid(String name) {
        int counter = 0;
        for (char c : name.toCharArray())
            counter += Character.isAlphabetic(c) ? 1 : 0;
        return counter > 5;
    }

    private boolean checkIfEmailAddressIsValid(String email) {
        return Pattern.compile("^(.+)@(.+)$").matcher(email).matches();
    }

    private boolean checkIfPasswordIsValid(String password) {
        return Pattern
                .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")
                .matcher(password).matches();
    }

    private void clearFields() {
        binding.RFullName.setText("");
        binding.REmail.setText("");
        binding.RPassword.setText("");
        binding.RCPassword.setText("");
        binding.LEmail.setText("");
        binding.RPassword.setText("");
    }
}