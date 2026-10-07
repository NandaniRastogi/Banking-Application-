package com.nandani.banking.entitiy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String transactionReference;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER

    }

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED

    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Size(max = 255)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount; // destination account
}
