package com.tien.iam_service2_keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.tien.iam_service2_keycloak.client")
public class IamService2KeycloakApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamService2KeycloakApplication.class, args);
    }

}
