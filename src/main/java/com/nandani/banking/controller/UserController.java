package com.nandani.banking.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.OtpDto;
import com.nandani.banking.dto.PhoneChangeRequest;
import com.nandani.banking.dto.PhoneOtpRequest;
import com.nandani.banking.dto.UpdateEmailRequest;
import com.nandani.banking.dto.UpdateUserProfileRequest;
import com.nandani.banking.dto.UserResponse;

import com.nandani.banking.service.EmailService;
import com.nandani.banking.service.OtpService;
import com.nandani.banking.service.PhoneNumberOtpService;

import com.nandani.banking.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final EmailService emailService;
    private final UserService userService;
    private final OtpService otpService;
    private final PhoneNumberOtpService phoneNumberOtpService;

    @PostMapping("/test")
    public ResponseEntity<String> emailTesting(@RequestParam String sentTo, @RequestParam String message,
            @RequestParam String subject) {

        emailService.sendEmail(sentTo, subject, message);
        return ResponseEntity.ok("Email Sent Successfully");

    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(userService.getUserDetails(authentication.getName()));

    }

    @PatchMapping("/profile")
    public ResponseEntity<String> updateMyProfile(
            @Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.UpdateUserDetails(authentication.getName(), updateUserProfileRequest);

        return ResponseEntity.ok("Updated successfully User Name");

    }

    @PatchMapping("/email")
    public ResponseEntity<String> requestEmailChange(@Valid @RequestBody UpdateEmailRequest updateEmailRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.UpdateUserEmail(authentication.getName(), updateEmailRequest);
        return ResponseEntity.ok(" Email Sent Succesfully ");

    }

    @PostMapping("/update-email-opt-verify")
    public ResponseEntity<String> requestEmailChange(@Valid @RequestBody OtpDto otpDto) {

        otpService.verifyOtp(otpDto.getEmail(), otpDto.getOtpDto());

        return ResponseEntity.ok("eMAIL Otp VERIFY successfully ");

    }

    @PostMapping("/me/phone-change-requests")
    public ResponseEntity<String> requestPhoneNumberChange(@Valid @RequestBody PhoneChangeRequest phoneChangeRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.UpdateUserPhoneNumber(email, phoneChangeRequest);

        return ResponseEntity.ok("OTP SENT Successfully");

    }

    @PostMapping("/me/phone-change-requests/verify")
    public ResponseEntity<String> requestPhoneNumberVerify(@Valid @RequestBody PhoneOtpRequest request) {

        phoneNumberOtpService.verifyPhoneChangeOtp(request.getPhone(), request.getOtp());

        return ResponseEntity.ok("Otp Verified and Mobile Number Updated Successfully");

    }

    @PatchMapping("/me/phone-change-requests/resend")
    public ResponseEntity<String> requestPhoneNumberResend(@Valid @RequestBody PhoneChangeRequest phoneChangeRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.UpdateUserPhoneNumber(email, phoneChangeRequest);

        return ResponseEntity.ok("resend OTP SUCCESSFULLY ");

    }

}
