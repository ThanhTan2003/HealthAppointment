package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.mapper.RoleMapper;
import com.programmingtechie.identity_service.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.identity_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {
    final RoleRepository roleRepository;
    final RoleMapper roleMapper;

    // Lấy tất cả danh sách
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    // Lấy tất cả danh sách ngoại trừ role có id là "NguoiDung"
    public List<RoleResponse> getAllRolesExceptNguoiDung() {
        return roleRepository.findAll()
                .stream()
                .filter(role -> !"NguoiDung".equals(role.getId()))
                .map(roleMapper::toRoleResponse)
                .toList();
    }
}
