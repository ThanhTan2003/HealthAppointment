package com.programmingtechie.customer_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Customer;

import java.util.Optional;
import java.util.List;
import com.programmingtechie.customer_service.model.Patient;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT customer FROM Customer customer")
    Page<Customer> getAllCustomer(Pageable pageable);

    @Query(value = "SELECT * FROM public.customer WHERE " +
            "unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(date_of_birth)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(email)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))", nativeQuery = true)
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);


    Optional<Customer> findByFullName(String fullName);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByPatients(List<Patient> patients);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
