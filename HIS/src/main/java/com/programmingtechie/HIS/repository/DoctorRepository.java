package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {}
