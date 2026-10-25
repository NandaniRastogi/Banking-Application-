package com.nandani.banking.dto;

import com.nandani.banking.entitiy.User.UserStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserStatusRequest {
    @NotBlank
    UserStatus status;

}
