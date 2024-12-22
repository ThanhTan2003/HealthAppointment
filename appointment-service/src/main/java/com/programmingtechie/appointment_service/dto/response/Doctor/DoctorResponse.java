package com.programmingtechie.appointment_service.dto.response.Doctor;

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
    private String qualificationName;
    private String gender;

    // Danh sách các chuyên khoa mà bác sĩ thuộc về
    // private List<SpecialtyResponse> specialties;

    // Danh sách học hàm, học vị của bác sĩ
    // private List<QualificationResponse> qualifications;
}
