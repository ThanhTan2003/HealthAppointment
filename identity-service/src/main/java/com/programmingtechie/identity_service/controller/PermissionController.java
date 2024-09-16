package com.programmingtechie.identity_service.controller;

import com.programmingtechie.identity_service.dto.request.PermissionRequest;
import com.programmingtechie.identity_service.dto.response.PermissionResponse;
import com.programmingtechie.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/identity/permission")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    final PermissionService permissionService;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('QuanTriVien')")
    PermissionResponse create(@RequestBody PermissionRequest request){
        return permissionService.create(request);
    }

    @GetMapping("/get-list")
    List<PermissionResponse> getAll(){
        return permissionService.getAll();
    }

    @DeleteMapping("/delete/{permission}")
    void delete(@PathVariable String permission){
        permissionService.delete(permission);
    }
}
