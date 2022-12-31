package com.example.linky.backend.cache;

import android.graphics.Bitmap;

import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.PlatformLink;
import com.example.linky.backend.services.UserDataService;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class UserDataCache {
    private static PlatformLink[] platformLinks = null;
    private static String fullName, email = null;
    private static ConcurrentLinkedDeque<String> connections = null;
    private static Bitmap qrCode = null;

    private static void init(DocumentSnapshot userData, Bitmap userQRCode) {
        if (userData != null) {
            fullName = userData.get("fullName", String.class);
            email = userData.get("email", String.class);
            connections = new ConcurrentLinkedDeque<>((List<String>) userData.get("connections"));
//            Collections.reverse(connections);
            qrCode = userQRCode;

            Map<String, String> links = (Map<String, String>) userData.get("links");
            assert links != null;

            platformLinks = new PlatformLink[links.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : links.entrySet())
                platformLinks[i ++] = new PlatformLink(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Should load this in MainActivity and wait for it to complete before navigating to the tabbed
     * menu
     */
    public static void loadCacheAsync(
            ILambda succeeded,
            ILambda failed
    ) {
        UserDataService userDataService = UserDataService.getInstance();
        userDataService.getUserData((Object... objects) -> {
            init((DocumentSnapshot) objects[0], (Bitmap) objects[1]);
            succeeded.call();
        }, (Object... objects) -> {
            failed.call();
        });
    }

    public static void reset() {
        platformLinks = null;
        fullName = null;
        email = null;
        connections = null;
    }

    public static PlatformLink[] getEditableLinks() {
        return platformLinks;
    }

    public static String getFullName() {
        return fullName;
    }

    public static String getEmail() {
        return email;
    }

    public static ConcurrentLinkedDeque<String> getConnections() {
        return connections;
    }

    public static Bitmap getQrCode() {
        return qrCode;
    }
}
