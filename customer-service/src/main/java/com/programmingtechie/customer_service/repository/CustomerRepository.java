package com.programmingtechie.customer_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.programmingtechie.customer_service.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer,String>{
    @Query("SELECT customer FROM Customer customer")
    Page<Customer> getAllCustomer(Pageable pageable);
}
