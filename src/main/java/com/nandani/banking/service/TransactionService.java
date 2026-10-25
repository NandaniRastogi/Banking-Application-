package com.nandani.banking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nandani.banking.dto.DepositRequest;
import com.nandani.banking.dto.TransactionResponseDto;
import com.nandani.banking.dto.TransactionWithdrawRequestDto;
import com.nandani.banking.dto.TransferRequest;
import com.nandani.banking.entitiy.Transaction;

public interface TransactionService {

    TransactionResponseDto deposit(DepositRequest request);

    TransactionResponseDto withdraw(TransactionWithdrawRequestDto request);

    TransactionResponseDto transfer(TransferRequest request);

    TransactionResponseDto getTransactionById(Long id);

    List<TransactionResponseDto> getMyTransactions();

    Page<Transaction> transactionGetByAdmin(
            Long userId,
            Pageable pageable);

}
