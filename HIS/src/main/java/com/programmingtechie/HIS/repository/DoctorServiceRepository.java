package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.DoctorService;

public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {}
