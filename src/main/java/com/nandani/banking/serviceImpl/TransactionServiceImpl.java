package com.nandani.banking.serviceImpl;

import com.nandani.banking.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.nandani.banking.exception.AccountIsNotActiveException;
import com.nandani.banking.exception.IdNotFoundException;
import com.nandani.banking.exception.InsufficientException;
import com.nandani.banking.exception.InvalideUserException;
import com.nandani.banking.exception.NotAllowedToAccessAnotherApi;
import com.nandani.banking.exception.ReceiverAccountIsNotException;
import com.nandani.banking.exception.SenderAccountIsNotActive;
import com.nandani.banking.exception.TransactionIdNotFoundException;
import com.nandani.banking.repository.AccountRepository;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.NotificationService;
import com.nandani.banking.service.TransactionService;

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

    public boolean isValidUser(Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        if (account.getUser().getId().equals(user.getId())) {

            return true;
        }

        return false;

    }

    @Transactional
    @Override
    public TransactionResponseDto deposit(DepositRequest request) {

        if (!isValidUser(request.getAccountId())) {
            throw new InvalideUserException("Invalid Details You are not authorized for the Transaction ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountIsNotActiveException("Your Account is not Active");
        }

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
    public TransactionResponseDto withdraw(TransactionWithdrawRequestDto request) {

        if (!isValidUser(request.getAccountId())) {
            throw new InvalideUserException("Invalid User ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountIsNotActiveException("Account is not Active  Exception");
        }

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
    public TransactionResponseDto transfer(com.nandani.banking.dto.TransferRequest request) {

        if (!isValidUser(request.getAccountId())) {
            throw new InvalideUserException("Invalid User ");
        }

        if (!isValidUser(request.getDestinationAccountId())) {
            throw new InvalideUserException("Invalid User ");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        Account destAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        if (account.getAccountStatus() != AccountStatus.ACTIVE)
            throw new SenderAccountIsNotActive("Sender Account is not Active  ");

        if (destAccount.getAccountStatus() != AccountStatus.ACTIVE)
            throw new ReceiverAccountIsNotException("Receiver Account is not Active  ");

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientException("Insufficient balance");
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
    public TransactionResponseDto getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionIdNotFoundException("Transaction id is not found Exception"));

        Account account = transaction.getAccount();

        if (!isValidUser(id))
            throw new NotAllowedToAccessAnotherApi("Not allowed to access another api");

        return modelmapper.map(transaction, TransactionResponseDto.class);

    }

    @Override
    public List<TransactionResponseDto> getMyTransactions() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Account account = accountRepository.findById(user.getId())
                .orElseThrow(() -> new IdNotFoundException("Id not Found Exception"));

        List<TransactionResponseDto> list = new ArrayList<>();

        for (Transaction transaction : account.getTransaction()) {
            list.add(modelmapper.map(transaction, TransactionResponseDto.class));
        }

        return list;
    }

    @Override
    public Page<Transaction> transactionGetByAdmin(
            Long userId,
            Pageable pageable) {

        Page<Transaction> transPage = transactionRepository.findById(userId, pageable);

        return transPage;

    }

}
