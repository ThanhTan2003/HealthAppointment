package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.request.PermissionRequest;
import com.programmingtechie.identity_service.dto.response.PermissionResponse;
import com.programmingtechie.identity_service.mapper.PermissionMapper;
import com.programmingtechie.identity_service.model.Permission;
import com.programmingtechie.identity_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
