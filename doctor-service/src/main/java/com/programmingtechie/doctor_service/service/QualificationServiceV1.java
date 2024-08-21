package com.programmingtechie.doctor_service.service;

import com.programmingtechie.doctor_service.repository.QualificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QualificationServiceV1 {
    final QualificationRepository qualificationRepository;
}
