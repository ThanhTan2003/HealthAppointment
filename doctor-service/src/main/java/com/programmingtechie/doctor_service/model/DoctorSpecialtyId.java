package com.programmingtechie.doctor_service.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSpecialtyId implements Serializable {
    private String doctorId;
    private String specialtyId;
}
