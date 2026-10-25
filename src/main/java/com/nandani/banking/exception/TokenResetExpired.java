package com.nandani.banking.exception;

public class TokenResetExpired extends RuntimeException {

    public TokenResetExpired(String message) {
        super(message);

    }

}
