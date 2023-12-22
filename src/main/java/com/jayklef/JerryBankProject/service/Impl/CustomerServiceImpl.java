package com.jayklef.JerryBankProject.service.Impl;

import com.jayklef.JerryBankProject.dto.BankResponse;
import com.jayklef.JerryBankProject.dto.CustomerRequest;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.service.CustomerService;
import com.jayklef.JerryBankProject.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public BankResponse createAccount(CustomerRequest customerRequest) {

        // create a account means save customer to the DB

        // validate if customer already has an account
        
        Customer newCustomer = Customer.builder()
                .firstname(customerRequest.getFirstname())
                .lastname(customerRequest.getLastname())
                .middleName(customerRequest.getMiddleName())
                .email(customerRequest.getEmail())
                .gender(customerRequest.getGender())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .telephone(customerRequest.getTelephone())
                .alternateNumber(customerRequest.getAlternateNumber())
                .address(customerRequest.getAddress())
                .state(customerRequest.getState())
                .build();


        return null;
    }
}
