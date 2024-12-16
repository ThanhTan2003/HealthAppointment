package com.programmingtechie.HIS.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "doctor_specialty")
public class DoctorSpecialty {
    @EmbeddedId
    private DoctorSpecialtyId id;

    @MapsId("doctorId") // Ánh xạ với khóa chính phức hợp
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @MapsId("specialtyId") // Ánh xạ với khóa chính phức hợp
    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;
}
