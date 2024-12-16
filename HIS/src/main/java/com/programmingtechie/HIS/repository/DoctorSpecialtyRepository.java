package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.DoctorSpecialty;
import com.programmingtechie.HIS.model.DoctorSpecialtyId;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, DoctorSpecialtyId> {}
