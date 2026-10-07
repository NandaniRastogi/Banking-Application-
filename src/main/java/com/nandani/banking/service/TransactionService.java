package com.nandani.banking.service;

import java.util.List;

import com.nandani.banking.dto.DepositRequest;
import com.nandani.banking.dto.TransactionResponseDto;
import com.nandani.banking.dto.TransactionWithdrawRequestDto;
import com.nandani.banking.dto.TransferRequest;

public interface TransactionService {

    TransactionResponseDto deposit(DepositRequest request) throws Exception;

    TransactionResponseDto withdraw(TransactionWithdrawRequestDto request) throws Exception;

    TransactionResponseDto transfer(TransferRequest request) throws Exception;

    TransactionResponseDto getTransactionById(Long id) throws Exception;

    List<TransactionResponseDto> getMyTransactions() throws Exception;

}
