package com.atachakki.exception.businessLogic;

public class OrderItemNameAlreadyExistException extends BusinessLogicException {
    public OrderItemNameAlreadyExistException(String name) {
        super("OrderItem name - "+name+" already exist", null);
    }
}
