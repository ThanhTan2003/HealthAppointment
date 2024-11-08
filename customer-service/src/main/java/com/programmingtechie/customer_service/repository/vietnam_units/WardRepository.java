package com.programmingtechie.customer_service.repository.vietnam_units;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.vietnam_units.Ward;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictCode(String code);
}
