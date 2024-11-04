package com.programmingtechie.identity_service.mapper;

import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    public RoleResponse toRoleResponse(Role role)
    {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
