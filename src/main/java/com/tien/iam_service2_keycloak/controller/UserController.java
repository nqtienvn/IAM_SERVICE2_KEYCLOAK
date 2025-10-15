package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.dto.request.RegisterRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.CreateUserResponse;
import com.tien.iam_service2_keycloak.dto.response.UserInformResponse;
import com.tien.iam_service2_keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping()
    public ApiResponse<CreateUserResponse> createUser(@RequestBody RegisterRequest registerRequest) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.createUser(registerRequest))
                .build();
    }
    @PutMapping("/{userId}")
    public ApiResponse<CreateUserResponse> updateUser(@RequestBody UpdateRequest updateRequest, @PathVariable Long userId) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.updateUser(updateRequest, userId))
                .build();
    }
    @DeleteMapping("/{userId}")
    public ApiResponse<Boolean> softDelete(@PathVariable Long userId) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .message("success")
                .result(userService.deleteUser(userId))
                .build();
    }
    @GetMapping()
    public Page<UserInformResponse> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        return userService.getAllUsers(page, size);
    }
}
