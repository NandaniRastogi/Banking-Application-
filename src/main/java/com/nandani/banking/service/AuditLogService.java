package com.nandani.banking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nandani.banking.dto.AuditLogResponse;

public interface AuditLogService {

    void record(
            String actorEmail,
            String action,
            String targetType,
            Long targetId,
            String details);

    Page<AuditLogResponse> getAllAuditLogs(Pageable pageable);

}