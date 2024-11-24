package com.programmingtechie.patient_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.patient_service.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByInsuranceId(String insuranceId);

    boolean existsByIdentificationCode(String identificationCode);

    @Query("SELECT p FROM Patient p WHERE p.customerId = :customerId")
    Page<Patient> findPatientByCustomerId(@Param("customerId") String customerId, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.email = :email")
    Page<Patient> findPatientByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.phoneNumber = :phoneNumber")
    Page<Patient> findPatientByPhoneNumber(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    Page<Patient> findByCustomerId(String customerId, Pageable pageable);

    boolean existsById(String id);
}
