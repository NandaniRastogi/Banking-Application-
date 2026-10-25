package com.nandani.banking.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String actorEmail,
        String action,
        String targetType,
        Long targetId,
        String details,
        LocalDateTime createdAt
        ) {
}