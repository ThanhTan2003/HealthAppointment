package com.programmingtechie.identity_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.model.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    public RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder().id(role.getId()).name(role.getName()).build();
    }
}
