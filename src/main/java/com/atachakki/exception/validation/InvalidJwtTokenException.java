package com.atachakki.exception.validation;

public class InvalidJwtTokenException extends ValidationException {
    public InvalidJwtTokenException(String message, Object data) {
        super(message, data);
    }
}
