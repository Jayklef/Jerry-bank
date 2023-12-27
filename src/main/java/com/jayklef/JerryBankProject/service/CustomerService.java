package com.jayklef.JerryBankProject.service;

import com.jayklef.JerryBankProject.dto.*;

public interface CustomerService {

    BankResponse createAccount(CustomerRequest customerRequest);

    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest transferRequest);
    BankResponse login(LoginDto loginDto);
}
