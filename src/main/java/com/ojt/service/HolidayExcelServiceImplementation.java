package com.ojt.service;

import com.ojt.entity.Holiday;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
@Service
public class HolidayExcelServiceImplementation {


    public List<Holiday> excelToHolidayList(InputStream is) {
        List<Holiday> holidayList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = getStringCellValue(row.getCell(1));
                String dateStr = getStringCellValue(row.getCell(2));

                if (name != null && dateStr != null) {
                    Holiday holiday = new Holiday();
                    holiday.setName(name);
                    holiday.setDate(LocalDate.parse(dateStr));
                    holidayList.add(holiday);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        return holidayList;
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Handle Excel date cells
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = cell.getDateCellValue().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return date.toString(); // returns yyyy-MM-dd
                } else {
                    // for numbers like staff ID
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue evaluatedValue = evaluator.evaluate(cell);
                if (evaluatedValue.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = cell.getDateCellValue().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return date.toString();
                } else {
                    return evaluatedValue.formatAsString().replaceAll("^\"|\"$", "");
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }


    private static Long getLongCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue(); // Convert numeric value to Long
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Long.parseLong(cell.getStringCellValue()); // Convert string to Long
            } catch (NumberFormatException e) {
                return null; // Handle if the string cannot be parsed to a number
            }
        }
        return null;
    }

}
