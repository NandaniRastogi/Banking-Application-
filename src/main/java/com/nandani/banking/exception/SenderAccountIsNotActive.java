package com.nandani.banking.exception;

public class SenderAccountIsNotActive extends RuntimeException {

    public SenderAccountIsNotActive(String message) {
        super(message);
    }

}
