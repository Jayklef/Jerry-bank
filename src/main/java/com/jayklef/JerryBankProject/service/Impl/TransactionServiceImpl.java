package com.jayklef.JerryBankProject.service.Impl;

import com.jayklef.JerryBankProject.dto.TransactionDto;
import com.jayklef.JerryBankProject.model.Transaction;
import com.jayklef.JerryBankProject.repository.TransactionRepository;
import com.jayklef.JerryBankProject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .transactionType(transactionDto.getTransactionType())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);
    }
}
