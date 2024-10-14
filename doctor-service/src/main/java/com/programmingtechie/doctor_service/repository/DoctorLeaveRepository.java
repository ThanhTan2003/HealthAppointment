package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.doctor_service.model.DoctorLeave;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, String> {}
