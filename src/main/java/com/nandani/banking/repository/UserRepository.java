package com.nandani.banking.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nandani.banking.entitiy.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByPhone(String phone);

    Page<User> findAll(Pageable pageable);

    boolean existsByEmail(String phone);

}
