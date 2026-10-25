package com.nandani.banking.serviceImpl;

import org.springframework.stereotype.Service;

import com.nandani.banking.service.LoginService;
import com.nandani.banking.service.OtpService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final OtpService otpService;

    @Override
    public void verifyViaOtp(String email) {
        otpService.generateAndSendOtp(email);

    }

}
