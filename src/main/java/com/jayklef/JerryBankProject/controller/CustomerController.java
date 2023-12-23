package com.jayklef.JerryBankProject.controller;

import com.jayklef.JerryBankProject.dto.BankResponse;
import com.jayklef.JerryBankProject.dto.CustomerRequest;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("create")
    public BankResponse createAccount(@RequestBody CustomerRequest customerRequest){
        return customerService.createAccount(customerRequest);
    }
}
