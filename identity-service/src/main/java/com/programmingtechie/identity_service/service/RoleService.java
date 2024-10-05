package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.request.RoleRequest;
import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {
    final RoleRepository roleRepository;
}
