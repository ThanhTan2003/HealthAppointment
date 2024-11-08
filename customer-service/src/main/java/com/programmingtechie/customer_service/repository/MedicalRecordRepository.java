package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.customer_service.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {}