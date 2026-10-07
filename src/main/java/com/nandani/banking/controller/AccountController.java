package com.nandani.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.CreateAccountRequest;
import com.nandani.banking.entitiy.Account;
import com.nandani.banking.mapper.AccountResponseMapper;
import com.nandani.banking.service.AccountService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> getAccountType(@RequestBody CreateAccountRequest createAccountRequest) {

        Account account = accountService.createAccount(createAccountRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AccountResponseMapper.accountResponsemethod(account));

    }

    @GetMapping
    public ResponseEntity<AccountResponse> getAccountById(@RequestParam Long id) throws Exception {

        Account account = accountService.getAccountById(id);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(AccountResponseMapper.accountResponsemethod(account));

    }

    @GetMapping("/list")
    public ResponseEntity<List<AccountResponse>> getMyAccountMethod() {

        List<Account> accounts = accountService.getMyAccounts();

        List<AccountResponse> accountResponseList = new ArrayList<>();

        for (Account account : accounts) {
            accountResponseList.add(AccountResponseMapper.accountResponsemethod(account));
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountResponseList);
    }

    @DeleteMapping
    public ResponseEntity<String> closeAccount(@RequestParam Long id) throws Exception {

        accountService.closeAccount(id);

        return null;

    }

}
