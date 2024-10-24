package com.programmingtechie.customer_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.Ward;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictCode(String code);
}
