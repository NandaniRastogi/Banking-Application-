package com.nandani.banking.serviceImpl;

import java.math.BigDecimal;
import java.security.SecureRandom;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.CreateAccountRequest;
import com.nandani.banking.entitiy.Account;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.Account.AccountStatus;
import com.nandani.banking.exception.IdNotFoundException;
import com.nandani.banking.exception.OnlyActiveAccountClosed;
import com.nandani.banking.exception.UnauthorizedAccountAccessException;
import com.nandani.banking.repository.AccountRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.AccountService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ModelMapper modelmapper;
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
    public Account getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        if (account.getUser().getId().equals(user.getId())) {
            return account;
        }

        throw new UnauthorizedAccountAccessException("You are not authorized to access this account");

    }

    @Override
    public List<Account> getMyAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        List<Account> accountList = accountRepository.findByUser(user);

        return accountList;

    }

    @Override
    public void closeAccount(Long id) {

        Account account = getAccountById(id);
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new OnlyActiveAccountClosed("Only active account can be closed");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

    }

    @Override
    public Page<AccountResponse> getAllRegisteredAccount(Long id, Pageable pageable) {

        Page<Account> page = accountRepository.findByUserId(id, pageable);

        return page.map(account -> modelmapper.map(account, AccountResponse.class));

    }

}
