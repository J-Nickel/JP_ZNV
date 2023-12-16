package jznv.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XLSXParser {

    public static ArrayList<ArrayList<String>> parse(String path) {
        try {
            File file = new File(path);

            FileInputStream fis = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(fis);

            Sheet sheet = wb.getSheetAt(0);

            ArrayList<ArrayList<String>> rows = new ArrayList<>();

            for (Row row : sheet) rows.add(readStr(row));

            wb.close();
            fis.close();
            return rows;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> readStr(Row row) {
        ArrayList<String> row_buffer = new ArrayList<>();
        for (Cell cell : row) {
            String s = switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> String.valueOf(cell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                default -> "";
            };
            row_buffer.add(s);
        }
        return row_buffer;
    }
}