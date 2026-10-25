package com.nandani.banking.exception;

public class PhoneNumberAlreadyExistedException extends RuntimeException {

    public PhoneNumberAlreadyExistedException(String message) {
        super(message);
    }

}
