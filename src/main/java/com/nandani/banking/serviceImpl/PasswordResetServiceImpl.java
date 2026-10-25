package com.nandani.banking.serviceImpl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.ForgotPasswordRequest;
import com.nandani.banking.dto.ResetPasswordRequest;
import com.nandani.banking.entitiy.PasswordResetToken;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.exception.EmailNotFoundException;
import com.nandani.banking.exception.TokenNotFoundException;
import com.nandani.banking.exception.TokenResetExpired;
import com.nandani.banking.repository.PasswordResetTokenRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.PasswordResetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateRandomToken(ForgotPasswordRequest forgotPasswordRequest) {

        User dbUser = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Wrong Email Message"));

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(dbUser)
                .orElse(new PasswordResetToken());

        SecureRandom secureRandom = new SecureRandom();

        byte[] byteArray = new byte[32];

        secureRandom.nextBytes(byteArray);

        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(byteArray);

        passwordResetToken.setToken(token);

        passwordResetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(15));

        passwordResetToken.setUser(dbUser);

        passwordResetTokenRepository.save(passwordResetToken);

        return token;

    }

    @Override
    public String validateAndResetPassword(ResetPasswordRequest resetPasswordRequest) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new TokenNotFoundException("Token not found Exception"));

        if (passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {

            throw new TokenResetExpired("Token Reset Expired");

        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);

        return "Password Updated Successfully";

    }

}
