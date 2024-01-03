package com.jayklef.JerryBankProject.controller;

import com.jayklef.JerryBankProject.dto.*;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/")
@Tag(name = "Customer Account Management API")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Operation(
            summary = "Creating a new Customer account",
            description = "Creating a new Customer and assigning id"
    )

    @ApiResponse(
            responseCode = "201",
            description = "Http status CREATED")
    @PostMapping("create")
    public BankResponse createAccount(@RequestBody CustomerRequest customerRequest){
        return customerService.createAccount(customerRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Checking a customer account balance, given an account number"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status SUCCESS")
    @GetMapping("balance")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return customerService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        return customerService.login(loginDto);
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Checking a customer account name, given an account number"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status SUCCESS")

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return customerService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Credit Customer Account",
            description = "Crediting a customer account, given a valid account number"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status SUCCESS")

    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return customerService.creditAccount(request);
    }

    @Operation(
            summary = "Debit a Customer",
            description = "Debiting a customer account, given a valid account number"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status SUCCESS")

    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return customerService.debitAccount(request);
    }

    @Operation(
            summary = "Transfer Money",
            description = "Transferring money from a customer account to another and also updating customer balance"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status SUCCESS")

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return customerService.transfer(transferRequest);
    }

    @GetMapping("all")
    public ResponseEntity<List<Customer>> findAllCustomers(){
        List<Customer> customers = customerService.findAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}
