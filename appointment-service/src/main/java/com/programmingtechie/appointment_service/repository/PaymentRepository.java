package com.programmingtechie.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {}
