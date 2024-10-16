package com.programmingtechie.identity_service.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.identity_service.dto.request.UserCreationRequest;
import com.programmingtechie.identity_service.dto.request.UserUpdateRequest;
import com.programmingtechie.identity_service.dto.response.ApiResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/identity/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    final UserService userService;

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

    // Tao user moi
    @PostMapping("/create")
    @PreAuthorize("hasRole('QuanTriVien')")
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@RequestBody @Valid UserCreationRequest request) {
        userService.createUser(request);
    }

    // Lay danh sach user
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('QuanTriVien')") // Chp phep nguoi QuanTriVien moi co the su dung
    public PageResponse<UserResponse> getUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return userService.getUsers(page, size);
    }

    // Lay thong tin user theo username
    @GetMapping("user-name/{userName}")
    // @PostAuthorize("hasRole('QuanTriVien') or returnObject.userName == authentication.name") // Cho phep QTV va nguoi
    // dung có cung user co the su dung
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.result.userName == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable("userName") String userName) {
        return userService.getUserByUserId(userName);
    }

    // Cap nhat thong tin user
    @PutMapping("update/{userName}")
    @PreAuthorize("hasRole('QuanTriVien')")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@Valid @PathVariable String userName, @RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
    }

    // Cap nhat thong tin user
    @PutMapping("update-password/{userName}")
    @PostAuthorize("returnObject.userName == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @PathVariable String userName, @RequestBody UserUpdateRequest request) {
        userService.updatePassword(request);
    }

    // Xoa thong tin user
    @DeleteMapping("delete/{userName}")
    @PostAuthorize("hasRole('QuanTriVien')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String userName) {
        userService.deleteUser(userName);
    }

    // Lay thong tin dang nhap
    @GetMapping("/get-info")
    @PostAuthorize("returnObject.userName == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getInfo() {
        return userService.getMyInfo();
    }
}
