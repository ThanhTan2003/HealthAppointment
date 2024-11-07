package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByInsuranceId(String insuranceId);

    boolean existsByIdentificationCode(String identificationCode);
}
