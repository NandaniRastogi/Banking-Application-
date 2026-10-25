package com.nandani.banking.exception;

public class InsufficientException extends RuntimeException {

    public InsufficientException(String message) {
        super(message);
    }
}
