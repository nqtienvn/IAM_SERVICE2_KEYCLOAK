package com.tien.iam_service2_keycloak.service.impl;

import com.tien.iam_service2_keycloak.entity.User;
import com.tien.iam_service2_keycloak.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {
    private final UserRepository userRepository;

    public ExcelService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public byte[] exportUsersToExcel(List<User> users) throws IOException {
        //tạo một workbook đại diện cho một file Excel
        Workbook workbook = new XSSFWorkbook(); //cai nay la tao file.xlsx cho excel, dung cho excel tu 2007 den hien tai
        //tao mot sheat moi trong work book,(day chinh la buoc ma tao mot bang tinh trong file excel)
        Sheet sheet = workbook.createSheet("Users");
        //tao tieu de export ta la SOLID va BLUE
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setFontName("Arial");
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        //dinh nghia cac cot thoi, chua add vao cell
        String[] columns = {"ID", "Username", "Email", "FirstName", "LastName", "Avatar Url"};
        //tao cac cell cho header
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true); //tu dong xuong dong trong cell khi noi dung qua dai

        int rowNumber = 1; //bat dau tu dong 1 dien cac du lieu tu data base vao file
        for (User user : users) {
            Row row = sheet.createRow(rowNumber++);
            // Tạo cell cho từng thuộc tính
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(user.getId());
            cell0.setCellStyle(dataStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(user.getUsername());
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(user.getEmail());
            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(user.getFirstName());
            cell3.setCellStyle(dataStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(user.getLastName());
            cell4.setCellStyle(dataStyle);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(user.getAvatarUrl());
            cell5.setCellStyle(dataStyle);
        }
        // tu dong dieu chinh do rong cho sheet
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // chuyen work book thanh mang byte va tra ve
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
    public List<User> importStudentsFromExcel(MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();

        //doc du lieu tu file
        InputStream inputStream = file.getInputStream();

        //Tạo Workbook từ InputStream
        Workbook workbook = new XSSFWorkbook(inputStream);

        //Lấy sheet đầu tiên (index 0)
        Sheet sheet = workbook.getSheetAt(0);

        //Lấy Iterator để duyệt qua các dòng
        Iterator<Row> rowIterator = sheet.iterator();

        //Bỏ qua dòng header (dòng đầu tiên)
        if (rowIterator.hasNext()) {
            rowIterator.next(); // Skip header row
        }

        // Duyệt qua các dòng còn lại
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowEmpty(row)) {
                continue;
            }
            User user = new User();
//            Cell cell0 = row.getCell(0);
//            if (cell0 != null) {
//                user.setId((long) getNumericCellValue(cell0));
//            }
            Cell cell1 = row.getCell(1);
            if (cell1 != null) {
                user.setUsername(getStringCellValue(cell1));
            }
            Cell cell2 = row.getCell(2);
            if (cell2 != null) {
                user.setEmail(getStringCellValue(cell2));
            }
            Cell cell3 = row.getCell(3);
            if (cell3 != null) {
                user.setFirstName(getStringCellValue(cell3));
            }
            Cell cell4 = row.getCell(4);
            if (cell4 != null) {
                user.setLastName(getStringCellValue(cell4));
            }
            Cell cell5 = row.getCell(5);
            if (cell5 != null) {
                user.setAvatarUrl(getStringCellValue(cell5));
            }
            user.setEnabled(true);
            user.setDeleted(false);
            userRepository.save(user);
            users.add(user);
        }
        workbook.close();
        return users;
    }

    // Phương thức helper: Lấy giá trị string từ cell
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        // Xử lý theo kiểu dữ liệu của cell
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Nếu là số, chuyển thành string
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Phương thức helper: Lấy giá trị số từ cell
    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    // Phương thức helper: Kiểm tra dòng có rỗng không
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
