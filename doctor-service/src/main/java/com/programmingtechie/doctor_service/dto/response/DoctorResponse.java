package com.programmingtechie.doctor_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorResponse {
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
