package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.request.RoleRequest;
import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.mapper.RoleMapper;
import com.programmingtechie.identity_service.repository.PermissionRepository;
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
    final PermissionRepository permissionRepository;
    final RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
