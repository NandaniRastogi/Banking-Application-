package com.nandani.banking.serviceImpl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.security.Security;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.CreateAccountRequest;
import com.nandani.banking.entitiy.Account;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.Account.AccountStatus;
import com.nandani.banking.repository.AccountRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.AccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Account createAccount(CreateAccountRequest request) {

        Account account = new Account();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found Exception"));

        String accountNumber;

        do {

            accountNumber = String.valueOf(1000000000L + new SecureRandom().nextLong(900000000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        account.setAccountNumber(accountNumber);

        account.setAccountStatus(Account.AccountStatus.ACTIVE);
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        return accountRepository.save(account);

    }

    @Override
    public Account getAccountById(Long id) throws Exception {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        if (account.getUser().getId().equals(user.getId())) {
            return account;
        }

        throw new Exception("You are not authorized to access this account");

    }

    @Override
    public List<Account> getMyAccounts() throws UsernameNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        List<Account> accountList = accountRepository.findByUser(user);

        return accountList;

    }

    @Override
    public void closeAccount(Long id) throws Exception {

        Account account = getAccountById(id);
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new Exception("Only active account can be closed");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

    }

}
