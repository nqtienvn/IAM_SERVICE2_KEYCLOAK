package com.tien.iam_service2_keycloak.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class UserInformResponse {
    String username;
    String email;
    String firstName;
    String lastName;
    boolean enabled;
    boolean deleted;
}
