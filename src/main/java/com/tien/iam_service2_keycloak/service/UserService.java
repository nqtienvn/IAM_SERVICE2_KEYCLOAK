package com.tien.iam_service2_keycloak.service;

import com.tien.iam_service2_keycloak.dto.request.RegisterRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateRequest;
import com.tien.iam_service2_keycloak.dto.response.CreateUserResponse;
import com.tien.iam_service2_keycloak.dto.response.UserInformResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    CreateUserResponse createUser(RegisterRequest registerRequest);
    CreateUserResponse updateUser(UpdateRequest updateRequest, Long userId);
    Boolean deleteUser(Long userId);
    Page<UserInformResponse> getAllUsers(int page, int size);
}
