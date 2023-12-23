package com.jayklef.JerryBankProject.repository;

import com.jayklef.JerryBankProject.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);

    Customer findByAccountNumber(String accountNumber);
}
