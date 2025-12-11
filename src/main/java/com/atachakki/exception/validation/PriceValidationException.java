package com.atachakki.exception.validation;

public class PriceValidationException extends ValidationException {
    public PriceValidationException(String message, String data) {
        super(message, data);
    }
}
