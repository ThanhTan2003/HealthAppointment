package com.programmingtechie.customer_service.service.vietnam_units;

import com.programmingtechie.customer_service.model.vietnam_units.AdministrativeUnit;
import com.programmingtechie.customer_service.repository.vietnam_units.AdministrativeUnitRepository;
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
