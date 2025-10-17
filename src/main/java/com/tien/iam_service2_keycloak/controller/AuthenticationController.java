package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.dto.request.AuthenticationRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.AuthenticationResponse;
import com.tien.iam_service2_keycloak.service.AuthenticationDefaultService;
import com.tien.iam_service2_keycloak.service.JwtService;
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
    private final AuthenticationDefaultService authenticationDefaultService;
    private final JwtService jwtService;
    @Value("${iam.keycloak.realm}")
    private String realm;
    @Value("${iam.keycloak.client-id}")
    private String clientId;
    @Value("${iam.keycloak.auth-server-url}")
    private String baseUrl;
    @Value("${iam.use-keycloak:false}")
    private boolean useKeycloak;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody(required = false) AuthenticationRequest userLogin) {
        if(useKeycloak) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setUri(baseUrl + "/realms/" + realm + "/protocol/openid-connect/auth" + "?client_id=" + clientId + "&response_type=code" + "&redirect_uri=http://localhost:8080/authentication");
           return ApiResponse.<AuthenticationResponse>builder()
                    .code(200)
                    .message("Link redirect url in keycloak")
                    .result(authenticationResponse)
                    .build();
        }
         return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("success")
                .result(authenticationDefaultService.login(userLogin))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<String> refresh(@RequestParam String refreshToken) {

        if(useKeycloak){
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Refresh Successfully!")
                    .result(keycloakService.refreshNewToken(refreshToken))
                    .build();
        }
        return ApiResponse.<String>builder()
                .code(200)
                .message("Refresh Successfully!")
                .result(jwtService.refreshAcessToken(refreshToken))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<AuthenticationResponse> logout(@RequestParam(name = "refresh_token", required = false) String refreshToken, @RequestHeader(value = "Authorization", required = false) String bearertoken) {

        if(useKeycloak) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setRefreshToken(keycloakService.logout(refreshToken));
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(200)
                    .message("Successfully!")
                    .result(authenticationResponse)
                    .build();
        }
        String token = bearertoken.substring(7);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("success")
                .result(authenticationDefaultService.logout(token))
                .build();

    }
    @PostMapping("/admin-token")
    public String getAdminToken() {
        if(useKeycloak) {
            return keyCloakServiceImplements.getAdminToken();
        }
        return "khong lay duoc admin token o che do default";
    }
}
