package com.programmingtechie.customer_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.model.AdministrativeRegion;
import com.programmingtechie.customer_service.repository.AdministrativeRegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

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
