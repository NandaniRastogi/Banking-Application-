package com.nandani.banking.dto;

import com.nandani.banking.entitiy.Account.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    @NotNull
    private AccountType accountType;
}