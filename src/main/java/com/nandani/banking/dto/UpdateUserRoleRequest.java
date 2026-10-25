package com.nandani.banking.dto;

import com.nandani.banking.entitiy.User.Role;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(@NotNull Role role) {
}