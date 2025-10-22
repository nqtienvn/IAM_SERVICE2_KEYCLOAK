package com.tien.iam_service2_keycloak.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    //cáu hình động trông yaml cơ
    //them token vao header de request
    @Bean
    public OpenAPI openAPI(@Value("${open.api.title}") String title,
                           @Value("${open.api.version}") String version,
                           @Value("${open.api.description}") String description,
                           @Value("${open.api.url}") String url,
                           @Value("${open.api.description-server-url}") String descriptionServerUrl) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description))
                .servers(List.of(new Server().url(url).description(descriptionServerUrl)));
//                .components(
//                        new Components()
//                                .addSecuritySchemes(
//                                        "bearerAuth",
//                                        new SecurityScheme()
//                                                .type(SecurityScheme.Type.HTTP)
//                                                .scheme("bearer")
//                                                .bearerFormat("JWT")))
//                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }
    @Bean
    public GroupedOpenApi groupOpenApi() {
        return GroupedOpenApi.builder()
                .group("api_service") //ten ben duoi title, //cai nay la chi dinh tung service mot trong thang microservice
                .packagesToScan("com.tien.iam_service2_keycloak.controller") //scan tat cả cac bean controller nam trong package de lay ve duoc toan bo cac enpoit
                .build();
    }
}
