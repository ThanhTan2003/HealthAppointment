package com.programmingtechie.customer_service.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByInsuranceId(String insuranceId);

    boolean existsByIdentificationCode(String identificationCode);

    // @Query("SELECT patient FROM Patient patient WHERE patient.customerId = :customerId")
    // Page<Patient> findPatientByCustomerId(@Param("customerId") String customerId, Pageable pageable);

    // @Query("SELECT patient FROM Patient patient WHERE patient.email = :email")
    // Page<Patient> findPatientByCustomerEmail(@Param("email") String email, Pageable pageable);

    // @Query("SELECT patient FROM Patient patient WHERE patient.phoneNumber = :phoneNumber")
    // Page<Patient> findPatientByCustomerPhoneNumber(@Param("email") String email, Pageable pageable);
}
