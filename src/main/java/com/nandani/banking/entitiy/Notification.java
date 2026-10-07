package com.nandani.banking.entitiy;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    public enum NotificationType {
        LOGIN_OTP,
        PASSWORD_RESET,
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER

    }

    public enum NotificationChannel {
        EMAIL,
        SMS,
        PUSH,
        WHATSAPP
    }

    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private String message;

    @NotNull
    @Column(nullable = false)
    private NotificationType notificationType;

    @NotNull
    @Column(nullable = false)
    private NotificationChannel notificationChannel;

    @NotNull
    @Column(nullable = false)
    private NotificationStatus notificationStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
