package com.nandani.banking.entitiy;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhoneOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    private User user;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String newPhoneNumber;

    @NotBlank
    private String otpHash;

    private LocalDateTime expiresAt;

    private int attemets;

    public enum PhoneStatus {

        PENDING,
        VERIFIED,
        EXPIRED,
        CANCELLED

    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private PhoneStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
