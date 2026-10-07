package com.nandani.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.transaction.TransactionStatus;

import com.nandani.banking.entitiy.Transaction.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private Long id;
    private String transactionReference;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String description;
    private LocalDateTime createdAt;
    private String accountNumber;
    private BigDecimal balance;
    private String destinationAccountNumber;

}
