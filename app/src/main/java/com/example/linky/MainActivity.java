package com.example.linky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.services.AuthService;
import com.example.linky.ui.dialogs.LoadingScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.linky.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AuthService authService;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authService = AuthService.getInstance();

        Intent navIntent = new Intent(this, AuthenticationActivity.class);
        navIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (!authService.userLoggedIn())
            startActivity(navIntent);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;
        NavController navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment_activity_main
        );
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}