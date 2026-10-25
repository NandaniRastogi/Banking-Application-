package com.nandani.banking.exception;

public class NotAllowedToAccessAnotherApi extends RuntimeException {

    public NotAllowedToAccessAnotherApi(String message) {
        super(message);
    }

}
