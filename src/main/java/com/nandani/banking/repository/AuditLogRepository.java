package com.nandani.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nandani.banking.entitiy.AuditLog;

public interface AuditLogRepository
                extends JpaRepository<AuditLog, Long> {
}