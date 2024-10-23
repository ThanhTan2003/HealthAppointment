package com.programmingtechie.doctor_service.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "doctor_qualification")
public class DoctorQualification {

    @EmbeddedId
    private DoctorQualificationId id;

    @ManyToOne
    @MapsId("doctorId") // Map trường doctorId từ khóa chính
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @MapsId("qualificationAbbreviation") // Map trường qualificationAbbreviation từ khóa chính
    @JoinColumn(name = "qualification_abbreviation", nullable = false)
    private Qualification qualification;
}
