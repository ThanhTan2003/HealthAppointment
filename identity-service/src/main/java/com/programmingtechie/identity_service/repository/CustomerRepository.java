package com.programmingtechie.identity_service.repository;

import com.programmingtechie.identity_service.model.Customer;
import com.programmingtechie.identity_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    List<Customer> findByStatus(String status);

    @Query("SELECT u FROM Customer u")
    Page<Customer> getAllCustomers(Pageable pageable);
}
