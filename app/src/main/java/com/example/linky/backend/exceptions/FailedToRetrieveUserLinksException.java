package com.example.linky.backend.exceptions;

public class FailedToRetrieveUserLinksException extends Exception {
    public FailedToRetrieveUserLinksException() {
        super("Failed to retrieve user links from cloud");
    }
}
