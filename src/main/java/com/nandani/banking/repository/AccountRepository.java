package com.nandani.banking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nandani.banking.entitiy.Account;
import com.nandani.banking.entitiy.User;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    Page<Account> findByUserId(Long userId, Pageable pageable);

}
