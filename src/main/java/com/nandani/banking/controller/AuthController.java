package com.nandani.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.ForgotPasswordRequest;
import com.nandani.banking.dto.LoginRequest;
import com.nandani.banking.dto.LoginResponse;
import com.nandani.banking.dto.OtpDto;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.dto.ResetPasswordRequest;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.service.LoginService;
import com.nandani.banking.service.OtpService;
import com.nandani.banking.service.PasswordResetService;
import com.nandani.banking.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final PasswordResetService passwordResetService;
    private final LoginService loginService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {

        return userService.registerUser(request);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        loginService.verifyViaOtp(request.getEmail());

        return ResponseEntity.ok("Your Password is verifid and please also verify OTP");

    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(
            @Valid @RequestBody OtpDto request) {

        return ResponseEntity.ok(
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtpDto()));
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return passwordResetService.generateRandomToken(forgotPasswordRequest);

    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        return passwordResetService.validateAndResetPassword(resetPasswordRequest);

    }

}
