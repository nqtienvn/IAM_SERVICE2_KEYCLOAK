package com.tien.iam_service2_keycloak.service.impl;

import com.tien.iam_service2_keycloak.dto.request.RegisterRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateRequest;
import com.tien.iam_service2_keycloak.dto.response.CreateUserResponse;
import com.tien.iam_service2_keycloak.dto.response.UserInformResponse;
import com.tien.iam_service2_keycloak.entity.User;
import com.tien.iam_service2_keycloak.exception.AppException;
import com.tien.iam_service2_keycloak.exception.ErrorCode;
import com.tien.iam_service2_keycloak.mapper.UserMapper;
import com.tien.iam_service2_keycloak.repository.UserRepository;
import com.tien.iam_service2_keycloak.service.KeycloakService;
import com.tien.iam_service2_keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE_IMPLEMENTS")
public class UserServiceImplements implements UserService {
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(RegisterRequest registerRequest) {
        String keycloakUserId = keycloakService.createUser(registerRequest);
        log.info("keycloakUserId:{}", keycloakUserId);
        User user = userMapper.toUser(registerRequest);
        user.setKeycloakUserId(keycloakUserId);
        user.setDeleted(false);
        user.setEnabled(true);
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }

    @Override
    public CreateUserResponse updateUser(UpdateRequest updateRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String keycloakUserId = user.getKeycloakUserId();
        keycloakService.updateUser(updateRequest, keycloakUserId);
        user.setEmail(updateRequest.getEmail());
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        User userUpdate = userRepository.save(user);
        log.info("Inform before update: {}", updateRequest);
        log.info("Inform after update: {}", user);
        return userMapper.toCreateUserResponse(userUpdate);
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String keycloakUserId = user.getKeycloakUserId();
        keycloakService.softDelete(keycloakUserId); //da enable roi nha
        user.setDeleted(true);
        userRepository.save(user);
        log.info("Inform before delete: {}", user.getUsername());
        return user.getDeleted();
    }

    @Override
    public Page<UserInformResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageUser = userRepository.findAll(pageable);
        List<UserInformResponse> dtoList = pageUser.getContent().stream()
                .map(user -> new UserInformResponse(
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEnabled(),
                        user.getDeleted()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, pageUser.getTotalElements());
    }
}
