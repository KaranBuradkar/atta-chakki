package com.atachakki.exception.validation;

import com.atachakki.exception.AppException;

public class ValidationException extends AppException {

    public ValidationException(String message, Object data) {
        super(message, data);
    }

}
