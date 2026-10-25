package com.nandani.banking.serviceImpl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nandani.banking.entitiy.PhoneOtp;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.Notification.NotificationChannel;
import com.nandani.banking.entitiy.Notification.NotificationType;
import com.nandani.banking.entitiy.PhoneOtp.PhoneStatus;
import com.nandani.banking.exception.EmailNotFoundException;
import com.nandani.banking.exception.GiverUserInOtpTableNotFoundException;
import com.nandani.banking.exception.OtpExpiredTimedOutException;
import com.nandani.banking.exception.OtpMismatchException;
import com.nandani.banking.exception.PhoneNumberAlreadyExistedException;
import com.nandani.banking.exception.UserNotFoundException;
import com.nandani.banking.repository.PhoneOtpRepostitory;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.NotificationService;
import com.nandani.banking.service.PhoneNumberOtpService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PhoneOtpServiceImpl implements PhoneNumberOtpService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final NotificationService notificationService;
        private final PhoneOtpRepostitory phoneOtpRepostitory;

        @Override
        @Transactional
        public void requestPhoneChange(
                        String currentUserEmail,
                        String newPhoneNumber) {
                // 1. Find current user
                User user = userRepository.findByEmail(currentUserEmail)
                                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

                // 2. Check if the new phone number is already registered
                if (userRepository.existsByPhone(newPhoneNumber)) {
                        throw new PhoneNumberAlreadyExistedException(
                                        "Phone number already exists. Please enter a new number");
                }

                // 3. Delete previous pending OTP, if any
                if (phoneOtpRepostitory.existsByUser(user)) {
                        phoneOtpRepostitory.deleteByUser(user);
                }

                // 4. Generate a secure 4-digit OTP
                SecureRandom secureRandom = new SecureRandom();
                String randomOtp = String.valueOf(
                                1000 + secureRandom.nextInt(9000));

                // 5. Create PhoneOtp entity
                PhoneOtp phoneOtp = new PhoneOtp();
                phoneOtp.setUser(user);
                phoneOtp.setNewPhoneNumber(newPhoneNumber);

                // Store the hashed OTP, not the original OTP
                phoneOtp.setOtpHash(passwordEncoder.encode(randomOtp));

                phoneOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
                phoneOtp.setStatus(PhoneStatus.PENDING);
                phoneOtp.setAttemets(0);

                // 6. Save the OTP request
                phoneOtpRepostitory.save(phoneOtp);

                // 7. Send the actual OTP through email
                notificationService.createNotification(
                                user,
                                "Your phone change verification OTP is: " + randomOtp
                                                + ". It expires in 5 minutes.",
                                NotificationType.EMAILVERIFICATION,
                                NotificationChannel.EMAIL);
        }

        @Override
        @Transactional
        public void verifyPhoneChangeOtp(
                        String currentUserNumber,
                        String otp) {

                String email = SecurityContextHolder.getContext().getAuthentication().getName();

                // 1. Find current user
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                // 2. Find pending phone OTP request
                PhoneOtp phoneOtp = phoneOtpRepostitory.findByUser(user)
                                .orElseThrow(() -> new GiverUserInOtpTableNotFoundException(
                                                "Phone OTP request not found"));

                // 3. Ensure request is pending
                if (phoneOtp.getStatus() != PhoneStatus.PENDING) {
                        throw new IllegalStateException(
                                        "This phone change request is not pending");
                }

                // 4. Check OTP expiry
                if (!phoneOtp.getExpiresAt().isAfter(LocalDateTime.now())) {
                        phoneOtp.setStatus(PhoneStatus.EXPIRED);
                        phoneOtpRepostitory.save(phoneOtp);

                        throw new OtpExpiredTimedOutException(
                                        "OTP expired. Please request a new OTP");
                }

                // 5. Check maximum attempts
                if (phoneOtp.getAttemets() >= 5) {
                        throw new OtpMismatchException(
                                        "Maximum OTP attempts exceeded. Request a new OTP");
                }

                // 6. Verify OTP
                if (!passwordEncoder.matches(otp, phoneOtp.getOtpHash())) {
                        phoneOtp.setAttemets(phoneOtp.getAttemets() + 1);
                        phoneOtpRepostitory.save(phoneOtp);

                        throw new OtpMismatchException("Invalid OTP");
                }

                // 7. Check phone number uniqueness again
                if (userRepository.existsByPhone(phoneOtp.getNewPhoneNumber())) {
                        throw new PhoneNumberAlreadyExistedException(
                                        "This phone number is already registered");
                }

                // 8. Update phone number
                user.setPhone(phoneOtp.getNewPhoneNumber());
                userRepository.save(user);

                // 9. Mark request as verified
                phoneOtp.setStatus(PhoneStatus.VERIFIED);
                phoneOtpRepostitory.save(phoneOtp);

        }
}
