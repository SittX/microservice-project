package com.kellot.microservices.order.exception;

public class QuantityInsufficientException extends RuntimeException {
    public QuantityInsufficientException(String message) {
        super(message);
    }

}
