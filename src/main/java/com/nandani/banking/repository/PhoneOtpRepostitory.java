package com.nandani.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nandani.banking.entitiy.PhoneOtp;
import com.nandani.banking.entitiy.User;

public interface PhoneOtpRepostitory extends JpaRepository<PhoneOtp, Long> {

    boolean existsByUser(User user);

    Optional<PhoneOtp> findByUser(User user);

    void deleteByUser(User user);

}
