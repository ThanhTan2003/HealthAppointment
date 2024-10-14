package com.programmingtechie.identity_service.config;

// import com.programmingtechie.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Dinh nghia lop cau hinh
@EnableWebSecurity // Kich hoat bao mat web
@EnableMethodSecurity // Kich hoat bao mat cho cac phuong thuc
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = { // Danh sach cac endpoints cong khai
        "/api/v1/identity/auth/log-in",
        "/api/v1/identity/auth/log-out",
        "/api/v1/identity/permission",
        "/api/v1/identity/auth/introspect",
        "/api/v1/identity/identity/role",
        "/api/v1/identity/auth/refresh"
    };

    @Autowired // Tiem CustomJwtDecoder tu Spring
    private CustomJwtDecoder customJwtDecoder; // Bien de su dung trong cau hinh JWT

    // Cau hinh cac endpoints co the truy cap ma khong can xac thuc
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cau hinh quyen truy cap cho cac endpoints
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                .permitAll() // Cho phep tat ca truy cap
                .anyRequest()
                .authenticated()); // Tat ca cac yeu cau khac phai xac thuc

        // Cau hinh server cho OAuth2 Resource Server
        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(customJwtDecoder)) // Su dung custom JWT decoder da duoc dinh nghia
                );

        // Vo hieu hoa CSRF
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build(); // Tra ve SecurityFilterChain da duoc xay dung
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() { // Dinh nghia converter cho JWT
        // Tao mot JwtGrantedAuthoritiesConverter de chuyen doi quyen truy cap
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Them tien to cho quyen (vi du: ROLE_USER)

        // Tao mot JwtAuthenticationConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                jwtGrantedAuthoritiesConverter); // Gan converter quyen cho JWT

        return jwtAuthenticationConverter; // Tra ve converter
    }

    @Bean
    PasswordEncoder passwordEncoder() { // Dinh nghia PasswordEncoder
        return new BCryptPasswordEncoder(10); // Su dung BCryptPasswordEncoder voi cost la 10
    }
}
