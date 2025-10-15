package com.tien.iam_service2_keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
