package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.dto.request.RegisterRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.RegisterResponse;
import com.tien.iam_service2_keycloak.service.KeycloakService;
import com.tien.iam_service2_keycloak.service.impl.KeyCloakServiceImplements;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final KeycloakService keycloakService;
    private final KeyCloakServiceImplements keyCloakServiceImplements;
    @Value("${iam.keycloak.realm}")
    private String realm;
    @Value("${iam.keycloak.client-id}")
    private String clientId;
    @Value("${iam.keycloak.auth-server-url}")
    private String baseUrl;
    @Value("${iam.use-keycloak:false}")
    private boolean useKeycloak;

    @GetMapping("/login")
    public String login() {
        if(useKeycloak) {
            return baseUrl + "/realms/" + realm + "/protocol/openid-connect/auth" + "?client_id=" + clientId + "&response_type=code" + "&redirect_uri=http://localhost:8080/authentication";
        }
        return "logic default";
    }

    @PostMapping("/refresh-token")
    public ApiResponse<String> refresh(@RequestParam String refreshToken) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Refresh Successfully!")
                .result(keycloakService.refreshNewToken(refreshToken))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestParam(name = "refresh_token") String refreshToken) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Successfully!")
                .result(keycloakService.logout(refreshToken))
                .build();
    }
    @PostMapping("/admin-token")
    public String getAdminToken() {
        return keyCloakServiceImplements.getAdminToken();
    }
}
