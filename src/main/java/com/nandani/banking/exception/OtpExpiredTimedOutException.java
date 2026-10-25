package com.nandani.banking.exception;

public class OtpExpiredTimedOutException extends RuntimeException {

    public OtpExpiredTimedOutException(String message) {
        super(message);
    }

}
