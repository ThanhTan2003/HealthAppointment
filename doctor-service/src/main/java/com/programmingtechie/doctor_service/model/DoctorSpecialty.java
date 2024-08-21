package com.programmingtechie.doctor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctor_specialty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSpecialty
{
    @Id
    private String doctorId;

    @Id
    private String specialtyId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "specialty_id", insertable = false, updatable = false)
    private Specialty specialty;
}
