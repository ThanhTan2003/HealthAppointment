package com.programmingtechie.customer_service.repository.vietnam_units;

import com.programmingtechie.customer_service.model.vietnam_units.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
