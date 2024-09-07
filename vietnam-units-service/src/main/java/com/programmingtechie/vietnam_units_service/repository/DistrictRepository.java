package com.programmingtechie.vietnam_units_service.repository;

import com.programmingtechie.vietnam_units_service.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
