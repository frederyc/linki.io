package com.example.linky.backend.models;

public class EditableLink {
    private String platform, link;

    public EditableLink() {
    }

    public EditableLink(String platform, String link) {
        this.platform = platform;
        this.link = link;
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
}
