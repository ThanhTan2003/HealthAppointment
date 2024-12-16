package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.Service;

public interface ServiceRepository extends JpaRepository<Service, String> {}
