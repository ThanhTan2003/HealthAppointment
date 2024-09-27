package com.programmingtechie.doctor_service.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQualificationId implements Serializable {
    private String doctorId;

    private String qualificationAbbreviation;
}
