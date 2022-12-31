package com.example.linky.backend.models;

import java.util.Map;

public class Connection {
    private String name, email, uuid;
    private Map<String, String> platformLinks;

    public Connection(String name, String email, String uuid, Map<String, String> platformLinks) {
        this.name = name;
        this.email = email;
        this.uuid = uuid;
        this.platformLinks = platformLinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, String> getPlatformLinks() {
        return platformLinks;
    }

    public void setPlatformLinks(Map<String, String> platformLinks) {
        this.platformLinks = platformLinks;
    }
}
