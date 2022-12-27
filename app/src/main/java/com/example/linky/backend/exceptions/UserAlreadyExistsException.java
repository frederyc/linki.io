package com.example.linky.backend.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("A user with this email address already exists");
    }
}
