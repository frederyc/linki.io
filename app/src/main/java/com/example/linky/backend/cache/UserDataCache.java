package com.example.linky.backend.cache;

import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.EditableLink;
import com.example.linky.backend.services.UserDataService;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDataCache {
    private static EditableLink[] editableLinks;
    private static String fullName, email;
    private static List<String> connections;

    private static void init(DocumentSnapshot userData) {
        if (userData != null) {
            fullName = userData.get("fullName", String.class);
            email = userData.get("email", String.class);
            connections = new ArrayList<>((List<String>) userData.get("connections"));

            Map<String, String> links = (Map<String, String>) userData.get("links");
            assert links != null;

            editableLinks = new EditableLink[links.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : links.entrySet())
                editableLinks[i ++] = new EditableLink(entry.getKey(), entry.getValue());
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
            init((DocumentSnapshot) objects[0]);
            succeeded.call((Object) editableLinks, fullName, email, connections);
        }, (Object... objects) -> {
            failed.call();
        });
    }
}
