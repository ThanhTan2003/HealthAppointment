package com.programmingtechie.doctor_service.mapper;

import com.programmingtechie.doctor_service.dto.response.QualificationResponse;
import com.programmingtechie.doctor_service.model.Qualification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QualificationMapper {
   public QualificationResponse toQualificationResponse(Qualification qualification) {
        return QualificationResponse.builder()
                .abbreviation(qualification.getAbbreviation())
                .name(qualification.getName())
                .displayOrder(qualification.getDisplayOrder())
                .build();
    }
}
