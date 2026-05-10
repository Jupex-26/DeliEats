package org.example.ordersservice.exception.custom;

public class QuantityExceedsException extends RuntimeException {
    public QuantityExceedsException(String message) {
        super(message);
    }
}