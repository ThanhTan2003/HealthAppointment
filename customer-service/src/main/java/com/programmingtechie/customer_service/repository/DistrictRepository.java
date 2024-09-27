package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
