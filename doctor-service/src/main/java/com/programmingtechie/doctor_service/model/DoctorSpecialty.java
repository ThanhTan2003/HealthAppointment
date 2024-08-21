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
    @EmbeddedId
    private DoctorSpecialtyId id;
}
