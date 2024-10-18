package com.programmingtechie.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import com.programmingtechie.api_gateway.repository.IdentityClient;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

// Lop cau hinh WebClient de cau hinh cac doi tuong can thiet cho viec ket noi den cac dich vu ben ngoai

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9090/api/v1/identity")
                .build();
    }

    @Bean
    CorsWebFilter corsWebFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }

    // Phuong thuc tao bean IdentityClient de lam viec voi dich vu Identity su dung WebClient
    @Bean
    IdentityClient identityClient(WebClient webClient) {
        // Su dung HttpServiceProxyFactory de tao doi tuong proxy cho viec goi HTTP
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(
                        WebClientAdapter.create(webClient)) // Tao WebClientAdapter tu WebClient da cau hinh o tren
                .build(); // Xay dung HttpServiceProxyFactory

        // Tao ra doi tuong IdentityClient de goi cac phuong thuc HTTP toi dich vu Identity
        return httpServiceProxyFactory.createClient(IdentityClient.class); // Tra ve doi tuong IdentityClient
    }
}
