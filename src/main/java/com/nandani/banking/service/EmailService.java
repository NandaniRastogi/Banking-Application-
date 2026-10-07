package com.nandani.banking.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String message);

}
