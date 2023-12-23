package com.jayklef.JerryBankProject.service.Impl;

import com.jayklef.JerryBankProject.dto.*;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.repository.CustomerRepository;
import com.jayklef.JerryBankProject.service.CustomerService;
import com.jayklef.JerryBankProject.service.EmailService;
import com.jayklef.JerryBankProject.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailService emailService;
    @Override
    public BankResponse createAccount(CustomerRequest customerRequest) {

        // create an account means save customer to the DB

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

        //send email alert

        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Account Creation")
                .messageBody("Congratulations, Your Account has been created successfully \n Your Account Info\n" +
                        "Account Name: " + customerNew.getFirstname().concat("").concat(customerNew.getLastname())
                        .concat("").concat(customerNew.getMiddleName()) + "\nAccount Number: " + customerNew.getAccountNumber())
                .build();
        emailService.sendEmailAlerts(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(customerNew.getAccountNumber())
                        .accountName(customerNew.getFirstname().concat(" ").concat(customerNew.getLastname()).concat(" ").concat(" ").concat(customerNew.getMiddleName()))
                        .accountBalance(customerNew.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExist = customerRepository.existsByAccountNumber(request.getAccountNumber());

        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Customer foundCustomer = customerRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundCustomer.getFirstname()+ " " + foundCustomer.getLastname()+ " "+ foundCustomer.getMiddleName())
                        .accountBalance(foundCustomer.getAccountBalance())
                        .build())
                .build();
    }
}
