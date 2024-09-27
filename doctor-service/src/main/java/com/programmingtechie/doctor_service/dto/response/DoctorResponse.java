package com.programmingtechie.doctor_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorResponse
{
    private String id;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String description;
    private String status;
    private LocalDateTime lastUpdated;

    // Danh sách các chuyên khoa mà bác sĩ thuộc về
    private List<SpecialtyResponse> specialties;

    // Danh sách học hàm, học vị của bác sĩ
    private List<QualificationResponse> qualifications;
}
