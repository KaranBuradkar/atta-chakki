package com.atachakki.exception.externalApi;

import com.atachakki.exception.AppException;

public class ExternalApiException extends AppException {

    public ExternalApiException(String message, Object data) {
        super(message, data);
    }

}
