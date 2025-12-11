package com.atachakki.exception.businessLogic;

public class UserAlreadyExistException extends BusinessLogicException {
    public UserAlreadyExistException(String username) {
        super("user already exist with username "+username, null);
    }
}
