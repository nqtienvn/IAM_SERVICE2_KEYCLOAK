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
     * DB: 5
     */
    ERROR_KEYCLOAK_USER(1, "create user in keycloak fail", HttpStatus.BAD_REQUEST),
    USER_FILTER(3, "user not found in db", HttpStatus.FORBIDDEN),
    ADMIN_TOKEN_NOT_FOUND(3, "not found admin access token", HttpStatus.BAD_REQUEST),
    CREATED_USER_KEYCLOAK_ERROR(1, "error created user in keycloak", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(9, "user not found", HttpStatus.BAD_REQUEST),
    PERMISSION_REPOSITORY_EMPTY(5, "PERMISSION REPOSITORY IS EMPTY", HttpStatus.BAD_REQUEST),
    ROLE_REPOSITORY_EMPTY(5, "ROLE REPOSITORY IS EMPTY", HttpStatus.BAD_REQUEST),
    PASS_INCORRECT(0, "PASS WORD INCORRECT", HttpStatus.BAD_REQUEST),
    ERROR_TOKEN(6, "TOKEN ERROR", HttpStatus.UNAUTHORIZED),
    UNKNOW_ERROR(3, "DON'T KNOW ERROR", HttpStatus.INTERNAL_SERVER_ERROR), //500
    NAME_EXIST(2, "NAME EXISTED", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(4, "ACCESSIONED", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(5, "NOT AUTHENTICATE USER", HttpStatus.UNAUTHORIZED),
    INVALID_OTP(0, "OTP INVALID", HttpStatus.BAD_REQUEST), //do du lieu request sai, khong dung cu phap de su ly
    INVALID_EMAIL(0, "EMAIL INVALID", HttpStatus.BAD_REQUEST),
    INVALID_LOGIN(0, "INVALID PASSWORD OR EMAIL", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(6, "TOKEN IS EXPIRED", HttpStatus.BAD_REQUEST),
    UNVERIFY_TOKEN(6, "TOKEN IS NOT VERIFY", HttpStatus.BAD_REQUEST),
    ERROR_UPLOAD_FILE(7, "ERROR UPLOAD FILE", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_MATCH_PASSWORD(0, "OLD PASS AND NEW PASS NOT MATCH", HttpStatus.BAD_REQUEST);
    ;
      int code;
      String message;
      HttpStatusCode httpStatusCode;
}
