package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.dto.request.CreateUserRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateRequest;
import com.tien.iam_service2_keycloak.dto.request.UserRoleRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.CreateUserResponse;
import com.tien.iam_service2_keycloak.dto.response.UserInformResponse;
import com.tien.iam_service2_keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping()
    public ApiResponse<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.createUser(createUserRequest))
                .build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ApiResponse<CreateUserResponse> updateUser(@RequestBody UpdateRequest updateRequest, @PathVariable Long userId) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.updateUser(updateRequest, userId))
                .build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ApiResponse<Boolean> softDelete(@PathVariable Long userId) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .message("success")
                .result(userService.deleteUser(userId))
                .build();
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public Page<UserInformResponse> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        return userService.getAllUsers(page, size);
    }

    @PostMapping("/lock/{userId}")
    @PreAuthorize("hasAuthority('USER_LOCK')")
    public ApiResponse<Boolean> blockUser(@PathVariable Long userId) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .message("success")
                .result(userService.blockUser(userId))
                .build();
    }

    @PostMapping("/un-lock/{userId}")
    @PreAuthorize("hasAuthority('USER_UNLOCK')")
    public ApiResponse<Boolean> unBlockUser(@PathVariable Long userId) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .message("success")
                .result(userService.unBlockUser(userId))
                .build();
    }
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_PROFILE')")
    public ApiResponse<CreateUserResponse> getUserDetail(@PathVariable Long userId) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.userDetail(userId))
                .build();
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('USER_ROLE_UPDATE')")
    public ApiResponse<CreateUserResponse> updateRole(@RequestBody UserRoleRequest userRoleRequest, @PathVariable(name = "id") Long id) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.updateRoleForUser(id, userRoleRequest))
                .build();
    }
    @PostMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('USER_ROLE_ADD')")
    public ApiResponse<CreateUserResponse> addRole(@RequestBody UserRoleRequest userRoleRequest, @PathVariable(name = "id") Long id) {
        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .message("success")
                .result(userService.addRoleUser(id, userRoleRequest))
                .build();
    }
}
