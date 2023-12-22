package com.jayklef.JerryBankProject.repository;

import com.jayklef.JerryBankProject.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
