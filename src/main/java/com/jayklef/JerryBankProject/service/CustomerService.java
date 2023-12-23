package com.jayklef.JerryBankProject.service;

import com.jayklef.JerryBankProject.dto.BankResponse;
import com.jayklef.JerryBankProject.dto.CustomerRequest;
import com.jayklef.JerryBankProject.dto.EnquiryRequest;

public interface CustomerService {

    BankResponse createAccount(CustomerRequest customerRequest);

    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
}
