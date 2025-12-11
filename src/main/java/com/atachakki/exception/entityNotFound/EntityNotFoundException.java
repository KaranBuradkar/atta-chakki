package com.atachakki.exception.entityNotFound;

import com.atachakki.exception.AppException;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String message, Object data) {
        super(message, data);
    }
}
