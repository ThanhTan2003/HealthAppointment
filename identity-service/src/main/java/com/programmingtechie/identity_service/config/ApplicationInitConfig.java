package com.programmingtechie.identity_service.config;

import com.programmingtechie.identity_service.enums.Role;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Role.QuanTriVien.name());

                User user = User.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .accountName("Admin")
                        .status("Đang hoạt động")
                        //.roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
