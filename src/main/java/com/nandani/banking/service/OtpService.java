package com.nandani.banking.service;

import com.nandani.banking.dto.LoginResponse;

public interface OtpService {

    void generateAndSendOtp(String email);

    LoginResponse verifyOtp(String email, String otp);
}
