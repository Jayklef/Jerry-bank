package com.jayklef.JerryBankProject.service.Impl;

import com.jayklef.JerryBankProject.dto.AccountInfo;
import com.jayklef.JerryBankProject.dto.BankResponse;
import com.jayklef.JerryBankProject.dto.CustomerRequest;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.repository.CustomerRepository;
import com.jayklef.JerryBankProject.service.CustomerService;
import com.jayklef.JerryBankProject.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public BankResponse createAccount(CustomerRequest customerRequest) {

        // create a account means save customer to the DB

        // validate if customer already has an account
        if (customerRepository.existsByEmail(customerRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

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

        Customer customerNew = customerRepository.save(newCustomer);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(customerNew.getAccountNumber())
                        .accountName(customerNew.getFirstname().concat(customerNew.getLastname()).concat(customerNew.getMiddleName()))
                        .accountBalance(customerNew.getAccountBalance())
                        .build())
                .build();

    }
}
