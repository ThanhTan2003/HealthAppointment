package com.programmingtechie.identity_service.config;

import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.programmingtechie.identity_service.enums.DefaultRoles;
import com.programmingtechie.identity_service.model.Role;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.RoleRepository;
import com.programmingtechie.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    final RoleRepository roleRepository;

    @Bean
    ApplicationRunner initializeRoles() {
        return args -> {
            // Khởi tạo các vai trò mặc định
            for (DefaultRoles defaultRole : DefaultRoles.values()) {
                if (roleRepository.findById(defaultRole.getId()).isEmpty()) {
                    Role role = Role.builder()
                            .id(defaultRole.getId())
                            .name(defaultRole.getName())
                            .build();
                    roleRepository.save(role);
                    log.info("Role {} has been created with name: {}", defaultRole.getId(), defaultRole.getName());
                }
            }
        };
    }

    @Bean
    ApplicationRunner initializeUser(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {

                // Tìm kiếm role với id là "QuanTriVien"
                Optional<Role> role = roleRepository.findById("QuanTriVien");

                if (role.isPresent()) {
                    // Tạo user mới với role
                    User user = User.builder()
                            .userName("admin")
                            .password(passwordEncoder.encode("admin")) // Mã hóa mật khẩu
                            .accountName("Admin")
                            .status("Đang hoạt động")
                            .role(role.get()) // Gán role
                            .build();

                    userRepository.save(user); // Lưu user vào cơ sở dữ liệu

                    log.warn("Admin user has been created with default password: admin, please change it");
                } else {
                    // Xử lý trường hợp không tìm thấy role
                    log.error("Role QuanTriVien not found, unable to create admin user");
                }
            }
        };
    }
}
