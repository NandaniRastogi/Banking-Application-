package com.nandani.banking.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.service.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final EmailService emailService;

    @GetMapping("/info")
    public String getMethodName() {

        return "Nandani Rastogi";

    }

    @PostMapping("/test")
    public ResponseEntity<String> emailTesting(@RequestParam String sentTo, @RequestParam String message,
            @RequestParam String subject) {

        emailService.sendEmail(sentTo, subject, message);
        return ResponseEntity.ok("Email Sent Successfully");

    }

}
