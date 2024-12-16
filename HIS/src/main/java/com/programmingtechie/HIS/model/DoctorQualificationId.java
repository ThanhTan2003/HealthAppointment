package com.programmingtechie.HIS.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQualificationId implements Serializable {
    private String doctorId;

    private String qualificationAbbreviation;
}
