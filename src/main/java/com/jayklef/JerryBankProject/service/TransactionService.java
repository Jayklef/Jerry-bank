package com.jayklef.JerryBankProject.service;

import com.jayklef.JerryBankProject.dto.TransactionDto;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
