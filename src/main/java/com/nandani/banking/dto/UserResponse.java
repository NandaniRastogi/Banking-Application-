package com.nandani.banking.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.management.relation.Role;

import com.nandani.banking.entitiy.Account;
import com.nandani.banking.entitiy.User.UserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;
    private List<Account> account;

}
