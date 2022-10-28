package com.example.linky.backend.models;

public class EditableLink {
    private String platform, link, userId;

    public EditableLink() {
    }

    public EditableLink(String platform, String link, String userId) {
        this.platform = platform;
        this.link = link;
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
