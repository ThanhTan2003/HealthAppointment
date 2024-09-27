package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.Ward;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictCode(String code);
}
