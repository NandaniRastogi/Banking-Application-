package com.nandani.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nandani.banking.entitiy.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
