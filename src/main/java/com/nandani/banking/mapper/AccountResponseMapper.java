package com.nandani.banking.mapper;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.entitiy.Account;

public class AccountResponseMapper {

    public static AccountResponse accountResponsemethod(Account account) {

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setAccountStatus(account.getAccountStatus());
        accountResponse.setAccountType(account.getAccountType());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.setId(account.getId());

        return accountResponse;

    }
}
