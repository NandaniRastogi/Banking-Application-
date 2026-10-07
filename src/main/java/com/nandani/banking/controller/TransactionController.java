package com.nandani.banking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.DepositRequest;
import com.nandani.banking.dto.TransactionResponseDto;
import com.nandani.banking.dto.TransactionWithdrawRequestDto;
import com.nandani.banking.dto.TransferRequest;
import com.nandani.banking.service.TransactionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> depositAmount(@Valid @RequestBody DepositRequest depositRequest)
            throws Exception {

        TransactionResponseDto transactionResponseDto = transactionService.deposit(depositRequest);

        return ResponseEntity.ok(transactionResponseDto);

    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdrawAmount(
            @Valid @RequestBody TransactionWithdrawRequestDto transactionWithdrawRequestDto) throws Exception {

        TransactionResponseDto transactionResponseDto = transactionService.withdraw(transactionWithdrawRequestDto);

        return ResponseEntity.ok(transactionResponseDto);

    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferAmount(
            @Valid @RequestBody TransferRequest transferRequest) throws Exception {

        TransactionResponseDto transactionResponseDto = transactionService.transfer(transferRequest);

        return ResponseEntity.ok(transactionResponseDto);

    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getMyTransaction() throws Exception {

        return ResponseEntity.ok(transactionService.getMyTransactions());
    }
}
