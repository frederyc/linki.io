package com.example.linky.backend.services;

import android.app.Activity;
import android.content.Intent;

import com.example.linky.R;
import com.example.linky.backend.interfaces.IAuthenticationFailed;
import com.example.linky.backend.interfaces.IAuthenticationSucceeded;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    private static AuthService instance = null;

    private final FirebaseAuth auth;

    private AuthService() {
        auth = FirebaseAuth.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null)
            instance = new AuthService();
        return instance;
    }

    public void createAccount(
            String email,
            String password,
            IAuthenticationSucceeded succeeded,
            IAuthenticationFailed failed
            ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                succeeded.call();
            else
                failed.call();
        });
    }

    public void signInWithEmailAndPassword(
            String email,
            String password,
            IAuthenticationSucceeded succeeded,
            IAuthenticationFailed failed
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful())
               succeeded.call();
           else
               failed.call();
        });
    }

}
