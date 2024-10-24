package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.AdministrativeUnit;

public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {}
