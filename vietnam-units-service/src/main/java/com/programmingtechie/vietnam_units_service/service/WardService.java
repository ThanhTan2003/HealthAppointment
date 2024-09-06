package com.programmingtechie.vietnam_units_service.service;

import com.programmingtechie.vietnam_units_service.model.Ward;
import com.programmingtechie.vietnam_units_service.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
