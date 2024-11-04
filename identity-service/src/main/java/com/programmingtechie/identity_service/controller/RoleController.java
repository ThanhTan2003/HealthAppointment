package com.programmingtechie.identity_service.controller;

import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.identity_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/identity/role")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    final RoleService roleService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // API lấy tất cả danh sách
    @GetMapping("/get-all")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    // API lấy tất cả danh sách nhưng trừ id là "NguoiDung"
    @GetMapping("/get-all-except-nguoi-dung")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public List<RoleResponse> getAllRolesExceptNguoiDung() {
        return roleService.getAllRolesExceptNguoiDung();
    }

}
