package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.client.StorageServiceClient;
import com.tien.iam_service2_keycloak.dto.request.FileFilterRequest;
import com.tien.iam_service2_keycloak.dto.request.GetImageRequest;
import com.tien.iam_service2_keycloak.dto.request.UpdateFileRequest;
import com.tien.iam_service2_keycloak.dto.response.ApiResponse;
import com.tien.iam_service2_keycloak.dto.response.FileS2Response;
import com.tien.iam_service2_keycloak.entity.User;
import com.tien.iam_service2_keycloak.exception.AppException;
import com.tien.iam_service2_keycloak.exception.ErrorCode;
import com.tien.iam_service2_keycloak.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@Tag(name = "Storage Service Controller")
public class StorageController {
    private final StorageServiceClient storageServiceClient;
    private final UserRepository userRepository;
    //phan quuyen ben nay luon
    @Operation(summary = "upload file",
    description = "upload file to cloudinary and manage in database")
    @PostMapping()
    @PreAuthorize("hasAuthority('USER_UPLOAD_AVATAR')")
    ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("typeOfFile") String typeOfFile) {
        //can mot cai token o header, no se het han lien tuc
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        ApiResponse<String> response = storageServiceClient.uploadFile(file, typeOfFile);
        User user = userRepository.findByUsername(userName).orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatarUrl(response.getResult());
        userRepository.save(user);
        return response;
    }
    @Operation(summary = "upload multi File",
            description = "upload multi file and manage by cloudinary")
    @PreAuthorize("hasAuthority('FILE_UPLOAD_MULTI')")
    @PostMapping(value = "/multi-file")
    ApiResponse<List<String>> uploadMultiFile(@RequestParam("files") MultipartFile[] files, @RequestParam("typeOfFile") String typeOfFile) {
        return storageServiceClient.uploadMultiFile(files, typeOfFile);
    }
    @Operation(summary = "delete a File",
            description = "delete a file in cloudinary and my database")
    @PreAuthorize("hasAuthority('FILE_DELETE')")
    @DeleteMapping()
    ApiResponse<String> deleteFile(@RequestParam String publicId) {
        return storageServiceClient.deleteFile(publicId);
    }
    @Operation(summary = "update a File",
            description = "update a file and manage by cloudinary")
    @PutMapping()
    @PreAuthorize("hasAuthority('USER_UPDATE_AVATAR')")
    ApiResponse<String> updateFile(@RequestParam MultipartFile newFile, @ModelAttribute UpdateFileRequest updateFileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        ApiResponse<String> response = storageServiceClient.updateFile(newFile, updateFileRequest);
        User user = userRepository.findByUsername(userName).orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatarUrl(response.getResult());
        userRepository.save(user);
        return response;
    }
    @PreAuthorize("hasAuthority('FILE_VIEW')")
    @Operation(summary = "get a File",
            description = "get a file and manage by cloudinary with radio or width, height")
    @GetMapping("/file-publicId")
    ApiResponse<String> getFile(@ModelAttribute GetImageRequest getImageRequest) {
        return storageServiceClient.getFile(getImageRequest);
    }
    @PreAuthorize("hasAuthority('FILE_FILTER')")
    @Operation(summary = "filter File",
            description = "filter file with filename, type, create date, modify date, owner")
    @GetMapping("/filter")
    ApiResponse<Page<FileS2Response>> filter(@ModelAttribute FileFilterRequest filterRequest) {
        return storageServiceClient.filter(filterRequest);
    }
}
