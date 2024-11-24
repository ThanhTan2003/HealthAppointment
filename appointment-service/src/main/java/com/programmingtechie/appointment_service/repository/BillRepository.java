package com.programmingtechie.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.model.Bill;

public interface BillRepository extends JpaRepository<Bill, String> {}
