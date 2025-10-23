package com.tien.iam_service2_keycloak.client;

import com.tien.iam_service2_keycloak.config.FeignClientConfig;
import com.tien.iam_service2_keycloak.dto.request.FileFilterRequest;
import com.tien.iam_service2_keycloak.dto.request.GetImageRequest;
import com.tien.iam_service2_keycloak.dto.request.GetProfileRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateFileRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.FileS2Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "storage-service",
        url = "http://localhost:8080",
        configuration = FeignClientConfig.class)
public interface StorageServiceClient {
    @PostMapping( value ="/api/cloudinary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<String> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("typeOfFile") String typeOfFile);

    @PostMapping(value = "/api/cloudinary/multi-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<List<String>> uploadMultiFile(@RequestPart("files") MultipartFile[] files, @RequestParam("typeOfFile") String typeOfFile);

    @DeleteMapping(value = "/api/cloudinary")
    ApiResponse<String> deleteFile(@RequestParam String publicId);

    @PutMapping(value = "/api/cloudinary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<String> updateFile(@RequestPart MultipartFile newFile, @SpringQueryMap UpdateFileRequest updateFileRequest);

    @GetMapping(value = "/api/cloudinary/file-publicId")
    ApiResponse<String> getFile(@SpringQueryMap GetImageRequest getImageRequest);

    @GetMapping(value = "/api/cloudinary/filter")
    ApiResponse<Page<FileS2Response>> filter(@SpringQueryMap FileFilterRequest filterRequest);

    @GetMapping(value = "/api/cloudinary/profile")
    ResponseEntity<ApiResponse<?>> getProfile(@SpringQueryMap GetProfileRequest getProfileRequest);
}
