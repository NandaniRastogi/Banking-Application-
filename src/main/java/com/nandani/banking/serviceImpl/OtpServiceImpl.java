package com.nandani.banking.serviceImpl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.LoginResponse;
import com.nandani.banking.entitiy.LogingOtp;
import com.nandani.banking.entitiy.Notification.NotificationChannel;
import com.nandani.banking.entitiy.Notification.NotificationType;
import com.nandani.banking.exception.EmailNotFoundException;
import com.nandani.banking.exception.MaximumOtpExceededException;
import com.nandani.banking.exception.OtpAlreadyUsedException;
import com.nandani.banking.exception.OtpExpiredTimedOutException;
import com.nandani.banking.exception.OtpMismatchException;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.repository.LoginOtpRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.securityCongiguration.JwtService;
import com.nandani.banking.service.NotificationService;
import com.nandani.banking.service.OtpService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginOtpRepository loginOtpRepository;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    @Override
    public void generateAndSendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Email Not Found! Please Enter Valid Email"));

        // Generate 6 digit OTP
        SecureRandom secureRandom = new SecureRandom();

        int otpNumber = 100000 + secureRandom.nextInt(900000);
        String otp = String.valueOf(otpNumber);

        // Never store raw OTP
        String otpHash = passwordEncoder.encode(otp);

        // Existing OTP mile to usi row ko update karo
        LogingOtp loginOtp = loginOtpRepository.findByUser(user).orElseGet(LogingOtp::new);

        loginOtp.setUser(user);
        loginOtp.setOtpHash(otpHash);
        loginOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        loginOtp.setAttempts(0);
        loginOtp.setVerified(false);

        loginOtpRepository.save(loginOtp);

        // Send OTP
        notificationService.createNotification(
                user,
                "Your OTP is: " + otp,
                NotificationType.LOGIN_OTP,
                NotificationChannel.EMAIL);
    }

    @Transactional
    @Override
    public LoginResponse verifyOtp(String email, String otp) {

        LogingOtp loginOtp = loginOtpRepository.findByUser_email(email)
                .orElseThrow(() -> new OtpMismatchException("You Enter the wrong Otp Enter the Valid Otp"));

        // 1. Maximum attempts
        if (loginOtp.getAttempts() >= 3) {
            throw new MaximumOtpExceededException(
                    "Maximum OTP attempts exceeded. Please request a new OTP");
        }

        // 2. OTP expiry
        if (loginOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredTimedOutException("OTP expired. Please request a new OTP");
        }

        // 3. OTP already verified
        if (loginOtp.isVerified()) {
            throw new OtpAlreadyUsedException("OTP has already been used");
        }

        // 4. Compare entered OTP with hashed OTP
        if (!passwordEncoder.matches(
                otp,
                loginOtp.getOtpHash())) {

            loginOtp.setAttempts(loginOtp.getAttempts() + 1);

            loginOtpRepository.save(loginOtp);

            throw new OtpMismatchException("Invalid OTP");
        }

        // 5. OTP correct
        loginOtp.setVerified(true);

        loginOtpRepository.save(loginOtp);

        // 6. Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 7. Generate JWT only after OTP verification
        String token = jwtService.generateToken(userDetails);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        return loginResponse;
    }
}