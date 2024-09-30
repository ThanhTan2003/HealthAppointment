package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.MedicalRecord;

public interface MedicalRecordRepository  extends JpaRepository<MedicalRecord,String>{
    
}
