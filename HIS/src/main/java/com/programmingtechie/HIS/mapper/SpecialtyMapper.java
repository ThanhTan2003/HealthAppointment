package com.programmingtechie.HIS.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.HIS.dto.response.SpecialtyResponse;
import com.programmingtechie.HIS.model.DoctorSpecialty;
import com.programmingtechie.HIS.model.Specialty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpecialtyMapper {
    // Chuyển đổi từ Specialty sang SpecialtyResponse
    public SpecialtyResponse toSpecialtyResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .specialtyId(specialty.getId())
                .specialtyName(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    // Hàm chuyển đổi từ Specialty sang SpecialtyResponse
    public SpecialtyResponse toSpecialtyResponse(DoctorSpecialty doctorSpecialty) {
        Specialty specialty = doctorSpecialty.getSpecialty();
        return SpecialtyResponse.builder()
                .specialtyId(specialty.getId())
                .specialtyName(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    // Chuyển đổi từ DTO SpecialtyResponse sang Entity Specialty
    public Specialty toSpecialtyEntity(SpecialtyResponse specialtyResponse) {
        return Specialty.builder()
                .id(specialtyResponse.getSpecialtyId())
                .name(specialtyResponse.getSpecialtyName())
                .description(specialtyResponse.getDescription())
                .build();
    }
}
