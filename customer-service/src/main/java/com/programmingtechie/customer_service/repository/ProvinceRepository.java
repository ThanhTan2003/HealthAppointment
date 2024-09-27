package com.programmingtechie.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.customer_service.model.Province;

public interface ProvinceRepository extends JpaRepository<Province, String> {
}
