package com.tien.iam_service2_keycloak.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    /** explain codes
     * key cloak: 1
     * crud user error: 9
     */
    ERROR_KEYCLOAK_USER(1, "create user in keycloak fail", HttpStatus.BAD_REQUEST),
    USER_FILTER(3, "user not found in db", HttpStatus.FORBIDDEN),
    ADMIN_TOKEN_NOT_FOUND(3, "not found admin access token", HttpStatus.BAD_REQUEST),
    CREATED_USER_KEYCLOAK_ERROR(1, "error created user in keycloak", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(9, "user not found", HttpStatus.BAD_REQUEST)
    ;
      int code;
      String message;
      HttpStatusCode httpStatusCode;
}
