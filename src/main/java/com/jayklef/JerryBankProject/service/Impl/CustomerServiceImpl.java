package com.jayklef.JerryBankProject.service.Impl;

import com.jayklef.JerryBankProject.dto.*;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.repository.CustomerRepository;
import com.jayklef.JerryBankProject.service.CustomerService;
import com.jayklef.JerryBankProject.service.EmailService;
import com.jayklef.JerryBankProject.service.TransactionService;
import com.jayklef.JerryBankProject.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

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
                        "Account Name: " + customerNew.getFirstname() + " "+ (customerNew.getLastname())+ " "+
                (customerNew.getMiddleName()) + "\nAccount Number: " + customerNew.getAccountNumber())
                .build();
        emailService.sendEmailAlerts(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(customerNew.getAccountNumber())
                        .accountName(customerNew.getFirstname()+ " "+(customerNew.getLastname()+ " "+(customerNew.getMiddleName())))
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

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = customerRepository.existsByAccountNumber(request.getAccountNumber());

        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }

        Customer foundCustomer = customerRepository.findByAccountNumber(request.getAccountNumber());
        return foundCustomer.getFirstname() + " "+ foundCustomer.getLastname()+ " "+
                foundCustomer.getMiddleName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {

        // check if account exists
        boolean isAccountExist = customerRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Customer customerToCredit = customerRepository.findByAccountNumber(request.getAccountNumber());
        customerToCredit.setAccountBalance(customerToCredit.getAccountBalance().add(request.getAmount()));

        customerRepository.save(customerToCredit);

        // Save transaction to DB

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(customerToCredit.getAccountNumber())
                .amount(request.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(customerToCredit.getFirstname()+ " "+ customerToCredit.getLastname()+" "
                        +customerToCredit.getLastname())
                        .accountBalance(customerToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExist = customerRepository.existsByAccountNumber(request.getAccountNumber());

        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Customer customerToDebit = customerRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = (customerToDebit.getAccountBalance().toBigInteger());
        BigInteger amountToDebit = (request.getAmount().toBigInteger());

        if (availableBalance.intValue() < amountToDebit.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else {
            customerToDebit.setAccountBalance(customerToDebit.getAccountBalance().subtract(request.getAmount()));
            customerRepository.save(customerToDebit);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(customerToDebit.getAccountNumber())
                    .amount(request.getAmount())
                    .transactionType("DEBIT")
                    .build();

            transactionService.saveTransaction(transactionDto);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(customerToDebit.getFirstname()+ " "+
                                    customerToDebit.getLastname()+ " "+
                                    customerToDebit.getMiddleName())
                            .accountBalance(customerToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        // get account to debit and check if it exists
        // check if amount is more than current balance
        // debit the account
        // get the account to credit
        // credit the account

        boolean isDestinationAccountExist = customerRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Customer sourceAcctCustomer = customerRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());

        if (transferRequest.getAmount().compareTo(sourceAcctCustomer.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAcctCustomer.setAccountBalance(sourceAcctCustomer.getAccountBalance()
                .subtract(transferRequest.getAmount()));

        String sourceAcctCustomerName = sourceAcctCustomer.getFirstname()+ " "+ sourceAcctCustomer.getLastname()+ " "+ sourceAcctCustomer.getMiddleName();

        customerRepository.save(sourceAcctCustomer);

        // send debit alert

        EmailDetails debitEmail = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(sourceAcctCustomer.getEmail())
                .messageBody("The sum of " + transferRequest.getAmount()+ " has been deducted from your account. Your current balance is  "+ sourceAcctCustomer.getAccountBalance())
                .build();

        emailService.sendEmailAlerts(debitEmail);

        Customer destinationAcctCustomer = customerRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAcctCustomer.setAccountBalance(destinationAcctCustomer.getAccountBalance().add(transferRequest.getAmount()));

        String recipientAcctCustomerName = destinationAcctCustomer.getFirstname()+ " "+ destinationAcctCustomer.getLastname()+ " "+ destinationAcctCustomer.getMiddleName();

        customerRepository.save(destinationAcctCustomer);

        // send credit email

        EmailDetails creditEmail = EmailDetails.builder()
                .subject("Credit Alert")
                .recipient(destinationAcctCustomer.getEmail())
                .messageBody("The sum of " + transferRequest.getAmount()+ " has been sent to your account from "+ sourceAcctCustomerName + "Your current balance is  "+ destinationAcctCustomer.getAccountBalance())
                .build();

        emailService.sendEmailAlerts(creditEmail);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAcctCustomer.getAccountNumber())
                .amount(transferRequest.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionDto);


        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
