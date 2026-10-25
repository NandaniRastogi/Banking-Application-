package com.nandani.banking.exception;

public class TransactionIdNotFoundException extends RuntimeException {

    public TransactionIdNotFoundException(String message) {
        super(message);
    }

}
