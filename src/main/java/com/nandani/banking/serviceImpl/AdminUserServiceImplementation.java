package com.nandani.banking.serviceImpl;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.AdminUserResponse;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.User.Role;
import com.nandani.banking.entitiy.User.UserStatus;
import com.nandani.banking.exception.UserNotFoundException;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.AdminUserServicee;
import com.nandani.banking.service.AuditLogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImplementation implements AdminUserServicee {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuditLogService auditLogService;

    @Override
    public AdminUserResponse getUserById(Long id) {

        User user = findUser(id);
        return modelMapper.map(user, AdminUserResponse.class);
    }

    @Override
    public Page<AdminUserResponse> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        return usersPage.map(user -> modelMapper.map(user, AdminUserResponse.class));

    }

    @Override
    @Transactional
    public AdminUserResponse updateUserStatus(
            Long id,
            UserStatus status) {
        User user = findUser(id);

        user.setStatus(status);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, AdminUserResponse.class);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public AdminUserResponse updateUserRole(Long id, Role role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, AdminUserResponse.class);

    }

    @Override
    @Transactional
    public AdminUserResponse updateUserStatusChangeTOAdminUserStatus(Long id, UserStatus status) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserStatus oldStatus = user.getStatus();

        user.setStatus(status);

        User savedUser = userRepository.save(user);

        String currentAdminEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        auditLogService.record(
                currentAdminEmail,
                "USER_STATUS_UPDATED",
                "USER",
                savedUser.getId(),
                "Status changed from " + oldStatus
                        + " to " + savedUser.getStatus());

        return modelMapper.map(savedUser, AdminUserResponse.class);
    }

}
