package com.example.linky.backend.services;

import com.example.linky.backend.interfaces.ILambda;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataService {
    private static UserDataService instance = null;
    private static final String COLLECTION_NAME = "userData";
    private final FirebaseFirestore db;

    private UserDataService() {
        db = FirebaseFirestore.getInstance();
    }

    public static UserDataService getInstance() {
        if (instance == null)
            instance = new UserDataService();
        return instance;
    }

    public void createUserDataInstance(
            String fullName,
            String uuid,
            String email,
            ILambda succeeded,
            ILambda failed
    ) {
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(uuid);

        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("fullName", fullName);
        dataToSave.put("connections", new ArrayList<String>());

        Map<String, String> socialNetworks = new HashMap<>();
        for (String socialNetwork : List.of("BeReal", "Discord", "Facebook", "Gmail", "Instagram",
                "LinkedIn", "OnlyFans", "Pinterest", "Quora", "Reddit", "Snapchat", "TikTok",
                "Twitter", "Yahoo"))
            socialNetworks.put(socialNetwork, null);

        if (email.split("@")[1].startsWith("gmail"))
            socialNetworks.put("Gmail", email);
        else if (email.split("@")[1].startsWith("yahoo"))
            socialNetworks.put("Yahoo", email);

        dataToSave.put("links", socialNetworks);
        docRef.set(dataToSave).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                succeeded.call();
            else
                failed.call();
        });
    }
}
