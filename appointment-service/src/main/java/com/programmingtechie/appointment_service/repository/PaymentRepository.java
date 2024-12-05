package com.programmingtechie.appointment_service.repository;

import com.programmingtechie.appointment_service.model.Bill;
import com.programmingtechie.appointment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

}
