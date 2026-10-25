package com.nandani.banking.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.AdminUserResponse;
import com.nandani.banking.dto.AuditLogResponse;
import com.nandani.banking.entitiy.AuditLog;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.User.UserStatus;
import com.nandani.banking.exception.UserNotFoundException;
import com.nandani.banking.repository.AuditLogRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.AuditLogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public void record(
            String actorEmail,
            String action,
            String targetType,
            Long targetId,
            String details) {

        AuditLog log = new AuditLog();

        log.setActorEmail(actorEmail);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDetails(details);

        auditLogRepository.save(log);
    }

    @Override
    @Transactional
    public Page<AuditLogResponse> getAllAuditLogs(Pageable pageable) {

        Page<AuditLog> aPage = auditLogRepository.findAll(pageable);
        return aPage.map(auditObj -> modelMapper.map(auditObj, AuditLogResponse.class));
    }

}