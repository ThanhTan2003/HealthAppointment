package com.programmingtechie.customer_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Customer;

import java.util.Optional;
import java.util.List;
import com.programmingtechie.customer_service.model.Patient;



@Repository
public interface CustomerRepository extends JpaRepository<Customer,String>{
    @Query("SELECT customer FROM Customer customer")
    Page<Customer> getAllCustomer(Pageable pageable);

    Optional<Customer> findByFullName(String fullName);

    List<Customer> findByPatientId(List<Patient> patientId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
