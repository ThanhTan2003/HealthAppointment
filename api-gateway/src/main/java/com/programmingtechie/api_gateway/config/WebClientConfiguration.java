package com.programmingtechie.api_gateway.config;

import com.programmingtechie.api_gateway.repository.IdentityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

// Lop cau hinh WebClient de cau hinh cac doi tuong can thiet cho viec ket noi den cac dich vu ben ngoai

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:9090/api/v1/identity")
                .build();
    }

    // Phuong thuc tao bean IdentityClient de lam viec voi dich vu Identity su dung WebClient
    @Bean
    IdentityClient identityClient(WebClient webClient) {
        // Su dung HttpServiceProxyFactory de tao doi tuong proxy cho viec goi HTTP
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)) // Tao WebClientAdapter tu WebClient da cau hinh o tren
                .build(); // Xay dung HttpServiceProxyFactory

        // Tao ra doi tuong IdentityClient de goi cac phuong thuc HTTP toi dich vu Identity
        return httpServiceProxyFactory.createClient(IdentityClient.class); // Tra ve doi tuong IdentityClient
    }
}
