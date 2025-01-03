package com.programmingtechie.patient_service.repository.vietnam_units;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.patient_service.model.vietnam_units.District;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
