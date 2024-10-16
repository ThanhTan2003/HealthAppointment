package com.programmingtechie.customer_service.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.model.AdministrativeUnit;
import com.programmingtechie.customer_service.repository.AdministrativeUnitRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdministrativeUnitService {
    final AdministrativeUnitRepository administrativeUnitRepository;

    public void createUnit(AdministrativeUnit unit) {
        administrativeUnitRepository.save(unit);
    }

    public List<AdministrativeUnit> getAllUnits() {
        return administrativeUnitRepository.findAll();
    }
}
