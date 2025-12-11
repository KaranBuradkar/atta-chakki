package com.atachakki.exception.businessLogic;

import com.atachakki.exception.AppException;

public class BusinessLogicException extends AppException {

    public BusinessLogicException(String message, Object data) {
        super(message, data);
    }

}
