package com.nandani.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.ForgotPasswordRequest;
import com.nandani.banking.dto.LoginRequest;
import com.nandani.banking.dto.LoginResponse;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.dto.ResetPasswordRequest;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.securityCongiguration.JwtService;
import com.nandani.banking.service.PasswordResetService;
import com.nandani.banking.service.UserService;
import com.nandani.banking.serviceImpl.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {

        return userService.registerUser(request);

    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        return loginResponse;

    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return passwordResetService.generateRandomToken(forgotPasswordRequest);

    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {

        return passwordResetService.validateAndResetPassword(resetPasswordRequest);

    }

}
