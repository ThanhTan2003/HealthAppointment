package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.DoctorQualificationId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.doctor_service.model.DoctorQualification;

public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, DoctorQualificationId> {}
