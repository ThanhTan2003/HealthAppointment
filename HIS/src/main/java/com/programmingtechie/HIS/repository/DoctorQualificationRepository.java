package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.DoctorQualification;
import com.programmingtechie.HIS.model.DoctorQualificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, DoctorQualificationId> {}
