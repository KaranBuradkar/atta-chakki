package com.atachakki.exception.databaseException;

import com.atachakki.exception.AppException;

public class DatabaseException extends AppException {

    public DatabaseException(String message, Object data) {
        super(message, data);
    }

}
