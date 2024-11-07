package com.programmingtechie.customer_service.service.vietnam_units;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.model.vietnam_units.Ward;
import com.programmingtechie.customer_service.repository.vietnam_units.WardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WardService {
    final WardRepository wardRepository;

    public void createWard(Ward ward) {
        wardRepository.save(ward);
    }

    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }

    public List<Ward> getByDistrict_Code(String code) {
        return wardRepository.findByDistrictCode(code);
    }
}
