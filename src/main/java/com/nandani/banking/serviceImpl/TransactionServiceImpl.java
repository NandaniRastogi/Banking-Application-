package com.nandani.banking.serviceImpl;

import com.nandani.banking.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.DepositRequest;
import com.nandani.banking.dto.TransactionResponseDto;
import com.nandani.banking.dto.TransactionWithdrawRequestDto;
import com.nandani.banking.entitiy.Account;
import com.nandani.banking.entitiy.Transaction;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.Account.AccountStatus;
import com.nandani.banking.entitiy.Notification.NotificationChannel;
import com.nandani.banking.entitiy.Notification.NotificationType;
import com.nandani.banking.entitiy.Transaction.TransactionStatus;
import com.nandani.banking.entitiy.Transaction.TransactionType;
import com.nandani.banking.repository.AccountRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.NotificationService;
import com.nandani.banking.service.TransactionService;
import com.nandani.banking.dto.TransferRequest;

import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ModelMapper modelmapper;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;

    public boolean isValidUser(Long id) throws Exception {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Account account = accountRepository.findById(id).orElseThrow(() -> new Exception("Id not Found Exception"));

        if (account.getUser().getId().equals(user.getId())) {

            return true;
        }

        return false;

    }

    @Transactional
    @Override
    public TransactionResponseDto deposit(DepositRequest request) throws Exception {

        if (!isValidUser(request.getAccountId())) {
            throw new Exception("Invalid User ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new Exception("Account is not Active  ");

        BigDecimal amount = account.getBalance().add(request.getAmount());

        account.setBalance(amount);

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setDescription("Amount Deposited");
        transaction.setTransactionReference(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.DEPOSIT);

        accountRepository.save(account);

        notificationService.createNotification(account.getUser(),
                "₹" + amount + " has been deposited into your account.", NotificationType.DEPOSIT,
                NotificationChannel.EMAIL);

        transactionRepository.save(transaction);

        return modelmapper.map(transaction, TransactionResponseDto.class);

    }

    @Transactional
    @Override
    public TransactionResponseDto withdraw(TransactionWithdrawRequestDto request) throws Exception {

        if (!isValidUser(request.getAccountId())) {
            throw new Exception("Invalid User ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new Exception("Account is not Active  ");

        // if(!(account.getBalance().compareTo())

        BigDecimal amount = account.getBalance().subtract(request.getAmount());

        account.setBalance(amount);

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setDescription("Amount Withdrawn");
        transaction.setTransactionReference(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.WITHDRAW);

        accountRepository.save(account);
        transactionRepository.save(transaction);

        notificationService.createNotification(
                account.getUser(),
                "₹" + amount + " has been withdrawn from your account.",
                NotificationType.WITHDRAWAL,
                NotificationChannel.EMAIL);

        return modelmapper.map(transaction, TransactionResponseDto.class);

    }

    @Transactional
    @Override
    public TransactionResponseDto transfer(com.nandani.banking.dto.TransferRequest request) throws Exception {

        if (!isValidUser(request.getAccountId())) {
            throw new Exception("Invalid User ");
        }

        if (!isValidUser(request.getDestinationAccountId())) {
            throw new Exception("Invalid User ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        Account destAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new Exception("Sender Account is not Active  ");

        if (destAccount.getAccountStatus() != AccountStatus.ACTIVE)
            throw new Exception("Receiver Account is not Active  ");

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new Exception("Insufficient balance");
        }

        destAccount.setBalance(destAccount.getBalance().add(request.getAmount()));
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setDestinationAccount(destAccount);
        transaction.setDescription("Amount Transferred");
        transaction.setTransactionReference(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.TRANSFER);

        accountRepository.save(account);
        transactionRepository.save(transaction);

        // 6. notification
        notificationService.createNotification(
                account.getUser(),
                "₹" + request.getAmount() + " has been deposited into your account.",
                NotificationType.DEPOSIT,
                NotificationChannel.EMAIL);

        return modelmapper.map(transaction, TransactionResponseDto.class);

    }

    @Override
    public TransactionResponseDto getTransactionById(Long id) throws Exception {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new Exception("Transaction id is not found Exception"));

        Account account = transaction.getAccount();

        if (!isValidUser(id))
            throw new Exception("Not allowed to access another api");

        return modelmapper.map(transaction, TransactionResponseDto.class);

    }

    @Override
    public List<TransactionResponseDto> getMyTransactions() throws Exception {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Account account = accountRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("Id not Found Exception"));

        List<TransactionResponseDto> list = new ArrayList<>();

        for (Transaction transaction : account.getTransaction()) {
            list.add(modelmapper.map(transaction, TransactionResponseDto.class));
        }

        return list;
    }

}
