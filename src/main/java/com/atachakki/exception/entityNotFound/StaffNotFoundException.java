package com.atachakki.exception.entityNotFound;

public class StaffNotFoundException extends EntityNotFoundException {
    public StaffNotFoundException(String message) {
        super(message, null);
    }
}
