package com.example.linky.backend.services;

import android.graphics.BitmapFactory;

import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.PlatformLink;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataService {
    private static UserDataService instance = null;
    private static final String COLLECTION_NAME = "userData";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final FirebaseStorage storage;
    private final QRCodeService qrCodeService;

    private UserDataService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        qrCodeService = QRCodeService.getInstance();
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
            if (task.isSuccessful()) {
                addQRCode((Object... objects) -> {
                    succeeded.call();
                }, (Object... objects) -> {
                    failed.call();
                });
            }
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
        docRef.get().addOnSuccessListener(documentSnapshot -> getQRCode(
            (Object... objects) -> succeeded.call(documentSnapshot, objects[0]),
            (Object... objects) -> failed.call()
        )).addOnFailureListener(e -> failed.call());
    }

    public void getConnectionsData(
            List<String> connectionsUUIDs,
            ILambda succeeded,
            ILambda failed
    ) {
        CollectionReference colRef = db.collection(COLLECTION_NAME);
        colRef.whereIn(
                FieldPath.documentId(),
                connectionsUUIDs.isEmpty() ? new ArrayList<>(List.of("NONE")) : connectionsUUIDs)
                .get().addOnSuccessListener(queryDocumentSnapshots ->
                        succeeded.call((List<DocumentSnapshot>) queryDocumentSnapshots.getDocuments())
                ).addOnFailureListener(e -> failed.call());
    }

    public void updateLink(
            PlatformLink platformLink,
            ILambda succeeded,
            ILambda failed
    ) {
        assert auth.getUid() != null;
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(auth.getUid());
        docRef.update(String.format("links.%s", platformLink.getPlatform()), platformLink.getLink())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        succeeded.call();
                    else
                        failed.call();
                });
    }

    public void addConnection(
            String uuid,
            ILambda succeeded,
            ILambda failed
    ) {
        assert auth.getUid() != null;
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(auth.getUid());
        docRef.update("connections", FieldValue.arrayUnion(uuid)).addOnCompleteListener(task -> {
           if (task.isSuccessful())
               succeeded.call();
           else
               failed.call();
        });
    }

    public void addQRCode(
            ILambda succeeded,
            ILambda failed
    ) {
        try {
            byte[] qrByteArray = qrCodeService.generateQR(auth.getUid());
            storage.getReference().child(String.format("%s.png", auth.getUid()))
                    .putBytes(qrByteArray).addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            succeeded.call();
                        else
                            failed.call();

                    });
        } catch (WriterException e) {
            e.printStackTrace();
            failed.call();
        }
    }

    public void getQRCode(
            ILambda succeeded,
            ILambda failed
    ) {
        storage.getReference().child(String.format("%s.png", auth.getUid()))
                .getBytes(5000).addOnSuccessListener(bytes -> {
                    succeeded.call(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }).addOnFailureListener(e -> {
                   e.printStackTrace();
                   failed.call();
                });
    }
}
