package com.nandani.banking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.CreateAccountRequest;
import com.nandani.banking.entitiy.Account;

public interface AccountService {

    Account createAccount(CreateAccountRequest request);

    Account getAccountById(Long id);

    List<Account> getMyAccounts();

    void closeAccount(Long id);

    Page<AccountResponse> getAllRegisteredAccount(Long id, Pageable pageable);

}
