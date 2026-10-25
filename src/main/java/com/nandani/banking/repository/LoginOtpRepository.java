package com.nandani.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nandani.banking.entitiy.LogingOtp;
import com.nandani.banking.entitiy.User;

import java.util.Optional;

public interface LoginOtpRepository extends JpaRepository<LogingOtp, Long> {

    Optional<LogingOtp> findByUser_email(String email);

    Optional<LogingOtp> findByUser(User user);

    void deleteByUser(User user);

}
