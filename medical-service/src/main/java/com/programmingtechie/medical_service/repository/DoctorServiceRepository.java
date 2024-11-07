package com.programmingtechie.medical_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.medical_service.model.DoctorService;

public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {}
