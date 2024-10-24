package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.AdministrativeRegion;

public interface AdministrativeRegionRepository extends JpaRepository<AdministrativeRegion, Integer> {}
