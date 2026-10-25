package com.nandani.banking.controller;

import org.modelmapper.internal.bytebuddy.TypeCache.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandani.banking.dto.AccountResponse;
import com.nandani.banking.dto.AdminUserResponse;
import com.nandani.banking.dto.TransactionResponseDto;
import com.nandani.banking.dto.UpdateUserRoleRequest;
import com.nandani.banking.dto.UpdateUserStatusRequest;
import com.nandani.banking.entitiy.Transaction;
import com.nandani.banking.exception.UserNotFoundException;
import com.nandani.banking.service.AccountService;
import com.nandani.banking.service.AdminUserServicee;
import com.nandani.banking.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

        private final AdminUserServicee adminUserService;
        private final AccountService accountService;
        private final TransactionService transactionService;

        @GetMapping("/{id}")
        public ResponseEntity<AdminUserResponse> getUserById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                adminUserService.getUserById(id));
        }

        @GetMapping
        public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
                        @PageableDefault(size = 10) Pageable pageable) {

                return ResponseEntity.ok(
                                adminUserService.getAllUsers(pageable));
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<AdminUserResponse> updateUserStatus(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateUserStatusRequest request) {

                return ResponseEntity.ok(
                                adminUserService.updateUserStatus(
                                                id,
                                                request.getStatus()));
        }

        @GetMapping("/{id}/accounts")
        public ResponseEntity<Page<AccountResponse>> getUserAccounts(
                        @PathVariable Long id,
                        @PageableDefault(size = 10) Pageable pageable) {

                return ResponseEntity.ok(
                                accountService.getAllRegisteredAccount(id, pageable));
        }

        @GetMapping("/{id}/transactions")
        public ResponseEntity<Page<Transaction>> getUserTransactions(
                        @PathVariable Long id,
                        @PageableDefault(size = 10) Pageable pageable) {

                return ResponseEntity.ok(transactionService.transactionGetByAdmin(id, pageable));

        }

        @PatchMapping("/{id}/role")
        public ResponseEntity<AdminUserResponse> updateUserRole(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateUserRoleRequest request) {

                return ResponseEntity.ok(
                                adminUserService.updateUserRole(id, request.role()));
        }

}
