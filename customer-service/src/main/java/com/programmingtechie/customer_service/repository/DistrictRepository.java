package com.programmingtechie.customer_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.District;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
