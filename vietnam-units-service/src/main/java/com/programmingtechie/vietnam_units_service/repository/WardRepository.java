package com.programmingtechie.vietnam_units_service.repository;

import com.programmingtechie.vietnam_units_service.model.District;
import com.programmingtechie.vietnam_units_service.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictCode(String code);
}
