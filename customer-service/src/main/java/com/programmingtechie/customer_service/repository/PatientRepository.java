package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.Patient;

public interface PatientRepository extends JpaRepository<Patient,String>{
    
}
