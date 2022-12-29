package com.example.linky.backend.services;

import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.EditableLink;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataService {
    private static UserDataService instance = null;
    private static final String COLLECTION_NAME = "userData";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    private UserDataService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static UserDataService getInstance() {
        if (instance == null)
            instance = new UserDataService();
        return instance;
    }

    public void createUserData(
            String fullName,
            ILambda succeeded,
            ILambda failed
    ) {
        String email = null, uuid = null;
        try {
            email = auth.getCurrentUser().getEmail();
            uuid = auth.getCurrentUser().getUid();
        } catch (NullPointerException e) {
            e.printStackTrace();
            failed.call();
            return;
        }

        assert email != null;

        DocumentReference docRef = db.collection(COLLECTION_NAME).document(uuid);
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("fullName", fullName);
        dataToSave.put("connections", new ArrayList<String>());
        dataToSave.put("email", email);

        Map<String, String> socialNetworks = new HashMap<>();
        for (String socialNetwork : List.of("BeReal", "Discord", "Facebook", "Gmail", "Instagram",
                "LinkedIn", "OnlyFans", "Pinterest", "Quora", "Reddit", "Snapchat", "TikTok",
                "Twitter", "Yahoo"))
            socialNetworks.put(socialNetwork, "");

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

    public void getUserData(
            ILambda succeeded,
            ILambda failed
    ) {
        String uuid = null;
        try {
            uuid = auth.getCurrentUser().getUid();
        } catch (NullPointerException e) {
            e.printStackTrace();
            failed.call();
            return;
        }

        DocumentReference docRef = db.collection(COLLECTION_NAME).document(uuid);
        docRef.get().addOnSuccessListener(documentSnapshot ->
            succeeded.call(documentSnapshot)
        ).addOnFailureListener(failure -> failed.call());
    }

    public void updateLink(
            EditableLink editableLink,
            ILambda succeeded,
            ILambda failed
    ) {
        assert auth.getUid() != null;
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(auth.getUid());
        docRef.update(String.format("links.%s", editableLink.getPlatform()), editableLink.getLink())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        succeeded.call();
                    else
                        failed.call();
                });
    }
}
