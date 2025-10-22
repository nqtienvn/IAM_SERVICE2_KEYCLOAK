package com.tien.iam_service2_keycloak.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileFilterRequest {
    String fileName;
    String typeOfFile;
    String createDate;
    String modifyDate;
    String owner;
    int page = 0;
    int size = 5;
}