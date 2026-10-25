package com.nandani.banking.service;

public interface PhoneNumberOtpService {

    public void requestPhoneChange(
            String currentUserEmail,
            String newPhoneNumber);

    public void verifyPhoneChangeOtp(
            String currentUserEmail,
            String otp);

}
