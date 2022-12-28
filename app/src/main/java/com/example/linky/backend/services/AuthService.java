package com.example.linky.backend.services;

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
                userDataService.createUserDataInstance(
                        fullName,
                        auth.getUid(),
                        email,
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

}
