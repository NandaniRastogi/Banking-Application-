package com.nandani.banking.exception;

public class AccountIsNotActiveException extends RuntimeException {

    public AccountIsNotActiveException(String message) {
        super(message);
    }

}
