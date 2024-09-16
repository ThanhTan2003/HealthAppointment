package com.programmingtechie.identity_service.controller;

import com.programmingtechie.identity_service.dto.request.RoleRequest;
import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/identity/role")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    final RoleService roleService;

    @PostMapping("/create")
    RoleResponse create(@RequestBody RoleRequest request){
        return roleService.create(request);
    }

    @GetMapping("/get-list")
    List<RoleResponse> getAll(){
        return roleService.getAll();
    }

    @DeleteMapping("/delete/{role}")
    void delete(@PathVariable String role){
        roleService.delete(role);
    }
}
