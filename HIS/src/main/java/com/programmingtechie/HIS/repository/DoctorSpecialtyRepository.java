package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.DoctorSpecialty;
import com.programmingtechie.HIS.model.DoctorSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, DoctorSpecialtyId> {}
