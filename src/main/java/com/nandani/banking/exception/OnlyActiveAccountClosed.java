package com.nandani.banking.exception;

public class OnlyActiveAccountClosed extends RuntimeException {

    public OnlyActiveAccountClosed(String message) {
        super(message);

    }

}
