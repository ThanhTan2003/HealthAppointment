package com.programmingtechie.vietnam_units_service.service;

import com.programmingtechie.vietnam_units_service.model.AdministrativeRegion;
import com.programmingtechie.vietnam_units_service.repository.AdministrativeRegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdministrativeRegionService {
    final AdministrativeRegionRepository administrativeRegionRepository;

    public void createRegion(AdministrativeRegion region) {
        administrativeRegionRepository.save(region);
    }

    public List<AdministrativeRegion> getAllRegions() {
        return administrativeRegionRepository.findAll();
    }
}
