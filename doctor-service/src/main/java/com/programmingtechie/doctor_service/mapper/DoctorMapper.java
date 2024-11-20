package com.programmingtechie.doctor_service.mapper;

import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.model.Doctor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorMapper {
    final SpecialtyMapper specialtyMapper;
    final QualificationMapper qualificationMapper;
    public DoctorResponse toDoctorResponse (Doctor doctor)
    {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .qualificationName(getQualificationName(doctor))
                .gender(doctor.getGender())
                .phoneNumber(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .description(doctor.getDescription())
                .status(doctor.getStatus())
                .image(doctor.getImage())
                .lastUpdated(doctor.getLastUpdated())
                .specialties(doctor.getSpecialties().stream()
                        .map(specialtyMapper::toSpecialtyResponse)
                        .collect(Collectors.toList()))
                .qualifications(doctor.getDoctorQualifications().stream()
                        .map(doctorQualification -> qualificationMapper.toQualificationResponse(doctorQualification.getQualification()))
                        .collect(Collectors.toList()))
                .build();
    }

    public DoctorResponse toDoctorResponsePublic (Doctor doctor)
    {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .qualificationName(getQualificationName(doctor))
                .gender(doctor.getGender())
                .phoneNumber(null)
                .email(null)
                .description(doctor.getDescription())
                .status(null)
                .image(doctor.getImage())
                .lastUpdated(null)
                .specialties(doctor.getSpecialties().stream()
                        .map(specialtyMapper::toSpecialtyResponse)
                        .collect(Collectors.toList()))
                .qualifications(doctor.getDoctorQualifications().stream()
                        .map(doctorQualification -> qualificationMapper.toQualificationResponse(doctorQualification.getQualification()))
                        .collect(Collectors.toList()))
                .build();
    }

    private String getQualificationName(Doctor doctor) {
        return doctor.getDoctorQualifications().stream()
                .map(doctorQualification ->
                        doctorQualification.getQualification().getAbbreviation())
                .collect(Collectors.joining(". "))
                .trim();
    }
}
