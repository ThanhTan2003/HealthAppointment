package com.programmingtechie.medical_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.medical_service.model.ServiceTimeFrame;

public interface ServiceTimeFrameRepository extends JpaRepository<ServiceTimeFrame, String> {}
