package com.programmingtechie.appointment_service.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Boolean existsByIdAndDate(String string, LocalDate now);
}
