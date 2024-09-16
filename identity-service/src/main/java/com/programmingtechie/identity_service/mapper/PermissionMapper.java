package com.programmingtechie.identity_service.mapper;

import com.programmingtechie.identity_service.dto.request.PermissionRequest;
import com.programmingtechie.identity_service.dto.response.PermissionResponse;
import com.programmingtechie.identity_service.model.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
