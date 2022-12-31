package com.example.linky.backend.cache;

import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.Connection;
import com.example.linky.backend.services.UserDataService;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionsDataCache {
    private static Map<String, Connection> connectionsData = new LinkedHashMap<>();

    private static void init(List<DocumentSnapshot> docsSnaps) {
        for (DocumentSnapshot docSnap : docsSnaps)
            connectionsData.put(docSnap.getId(), new Connection(
                    docSnap.get("fullName", String.class),
                    docSnap.get("email", String.class),
                    docSnap.getId(),
                    (Map<String, String>) docSnap.get("links")
            ));
    }

    public static void loadDataAsync(
            List<String> uuids,
            ILambda succeeded,
            ILambda failed
    ) {
        UserDataService userDataService = UserDataService.getInstance();
        userDataService.getConnectionsData(uuids, (Object... objects) -> {
            init((List<DocumentSnapshot>) objects[0]);
            succeeded.call();
        }, (Object... objects) -> {
            failed.call();
        });
    }

    public static void reset() {
        connectionsData.clear();
    }

    public static Map<String, Connection> getConnectionsData() {
        return connectionsData;
    }

    public static void setConnectionsData(Map<String, Connection> connectionsData) {
        ConnectionsDataCache.connectionsData = connectionsData;
    }
}
