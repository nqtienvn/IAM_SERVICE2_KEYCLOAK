package com.tien.iam_service2_keycloak.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FileS2Response {
        Long id;
        String fileName;
        String type;
        String url;
        Instant createdBy;
        Instant createdDate;
        String publicId;
    }
