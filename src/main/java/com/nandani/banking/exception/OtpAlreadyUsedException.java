package com.nandani.banking.exception;

public class OtpAlreadyUsedException extends RuntimeException {

    public OtpAlreadyUsedException(String message) {
        super(message);
    }

}
