package com.example.linky.backend.services;

import com.example.linky.backend.cache.ConnectionsDataCache;
import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.interfaces.ILambda;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    private static AuthService instance = null;
    private final FirebaseAuth auth;
    private final UserDataService userDataService;

    private AuthService() {
        auth = FirebaseAuth.getInstance();
        userDataService = UserDataService.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null)
            instance = new AuthService();
        return instance;
    }

    public void createAccount(
            String fullName,
            String email,
            String password,
            ILambda succeeded,
            ILambda failed
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                userDataService.createUserData(
                        fullName,
                        succeeded,
                        failed
                );
            else
                failed.call();
        });
    }

    public void signInWithEmailAndPassword(
            String email,
            String password,
            ILambda succeeded,
            ILambda failed
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful())
               succeeded.call();
           else
               failed.call();
        });
    }

    /**
     * Checks if a user is logged in, returns true if it is
     */
    public boolean userLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public void signOut() {
        auth.signOut();
        UserDataCache.reset();
        ConnectionsDataCache.reset();
    }

}
