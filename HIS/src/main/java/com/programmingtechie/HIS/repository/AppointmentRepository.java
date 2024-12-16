package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {}
