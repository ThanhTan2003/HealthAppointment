package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Patient;

import java.util.Optional;
import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient,String>{
    boolean existsByInsuranceId(String insuranceId);

    boolean existsByIdentificationCodeOrPassport(String identificationCodeOrPassport);
}