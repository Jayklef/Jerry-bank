package com.jayklef.JerryBankProject.service;

import com.jayklef.JerryBankProject.dto.BankResponse;
import com.jayklef.JerryBankProject.dto.CustomerRequest;

public interface CustomerService {

    BankResponse createAccount(CustomerRequest customerRequest);
}
