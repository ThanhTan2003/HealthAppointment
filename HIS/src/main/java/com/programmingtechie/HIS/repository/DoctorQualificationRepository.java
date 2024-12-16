package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.DoctorQualification;
import com.programmingtechie.HIS.model.DoctorQualificationId;

public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, DoctorQualificationId> {}
