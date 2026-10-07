package com.nandani.banking.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.CreateAccountRequest;
import com.nandani.banking.entitiy.Account;

public interface AccountService {

    Account createAccount(CreateAccountRequest request);

    Account getAccountById(Long id) throws Exception;

    List<Account> getMyAccounts() throws UsernameNotFoundException;

    void closeAccount(Long id) throws Exception;

    // Stri checkBalance(Long id);

}
