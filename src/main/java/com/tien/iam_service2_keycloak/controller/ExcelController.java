package com.tien.iam_service2_keycloak.controller;

import com.tien.iam_service2_keycloak.entity.User;
import com.tien.iam_service2_keycloak.repository.UserRepository;
import com.tien.iam_service2_keycloak.service.impl.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;
    private final UserRepository userRepository;
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportToExcel() throws IOException {
        // Giả lập dữ liệu - trong thực tế em sẽ lấy từ database
        List<User> users = userRepository.findAll();

        // Gọi service để tạo file Excel
        byte[] excelData = excelService.exportUsersToExcel(users);

        // Tạo resource từ mảng byte
        ByteArrayResource resource = new ByteArrayResource(excelData);

        // Thiết lập headers cho response
        //dinh dang header Content_DISPostion de cho phep tai xuong hay la xem truc tiep
        //attachment: tai xuong
        //inline: xem truc tiep
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_iam_service.xlsx");

        // Trả về ResponseEntity với file Excel
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelData.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
    @PostMapping("/import")
    public ResponseEntity<?> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Kiểm tra file có rỗng không
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty!");
            }

            // Kiểm tra định dạng file
            String contentType = file.getContentType();
            if (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest().body("Only .xlsx files are allowed!");
            }

            // Gọi service để import
            List<User> students = excelService.importStudentsFromExcel(file);

            // Trả về kết quả
            return ResponseEntity.ok()
                    .body("Import successful! Total students: " + students.size() + "\nData: " + students);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing file: " + e.getMessage());
        }
    }
}
