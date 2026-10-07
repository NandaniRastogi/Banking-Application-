package com.nandani.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nandani.banking.entitiy.Account.AccountStatus;
import com.nandani.banking.entitiy.Account.AccountType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private AccountType accountType;

    private AccountStatus accountStatus;

    private LocalDateTime createdAt;
}