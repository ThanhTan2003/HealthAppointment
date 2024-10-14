package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.doctor_service.model.Qualification;

public interface QualificationRepository extends JpaRepository<Qualification, String> {}
