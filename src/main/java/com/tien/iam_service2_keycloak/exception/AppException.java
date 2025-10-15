package com.tien.iam_service2_keycloak.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
  private ErrorCode errorCode;
  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
