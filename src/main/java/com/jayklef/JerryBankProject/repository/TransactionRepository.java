package com.jayklef.JerryBankProject.repository;

import com.jayklef.JerryBankProject.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
