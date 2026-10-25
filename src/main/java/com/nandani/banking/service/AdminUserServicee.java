package com.nandani.banking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nandani.banking.dto.AdminUserResponse;
import com.nandani.banking.entitiy.User.Role;
import com.nandani.banking.entitiy.User.UserStatus;

public interface AdminUserServicee {

    AdminUserResponse getUserById(Long id);

    Page<AdminUserResponse> getAllUsers(Pageable pageable);

    AdminUserResponse updateUserStatus(
            Long id,
            UserStatus status);

    AdminUserResponse updateUserRole(Long id, Role role);

    AdminUserResponse updateUserStatusChangeTOAdminUserStatus(Long id, UserStatus status);

}
