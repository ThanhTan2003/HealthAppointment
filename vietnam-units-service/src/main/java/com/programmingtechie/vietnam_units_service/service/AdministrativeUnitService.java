package com.programmingtechie.vietnam_units_service.service;

import com.programmingtechie.vietnam_units_service.model.AdministrativeUnit;
import com.programmingtechie.vietnam_units_service.repository.AdministrativeUnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
