package com.programmingtechie.doctor_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.doctor_service.repository.DoctorSpecialtyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorSpecialtyServiceV1 {
    final DoctorSpecialtyRepository doctorSpecialtyRepository;
}
