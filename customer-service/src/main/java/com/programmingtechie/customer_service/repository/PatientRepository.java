package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.Patient;

import java.util.Optional;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByInsuranceId(String insuranceId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Patient p WHERE p.identificationCodeOrPassport = :identificationCodeOrPassport")
    boolean existsByIdentificationCodeOrPassport(
            @Param("identificationCodeOrPassport") String identificationCodeOrPassport);

    //boolean existsByIdentificationCodeOrPassport(String identificationCodeOrPassport);
}