package jznv.io;

import jznv.entity.Student;
import jznv.entity.StudentTaskStat;
import jznv.entity.Task;
import jznv.entity.Theme;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class XLSXParser {
    private static final HashSet<String> INVALID_TASK_NAMES = new HashSet<>() {{
        add("Акт");
        add("Упр");
        add("ДЗ");
        add("Сем");
    }};

    @Getter
    private final List<Theme> themes = new ArrayList<>();
    @Getter
    private final List<Task> tasks = new ArrayList<>();
    @Getter
    private final List<Student> students = new ArrayList<>();
    @Getter
    private final List<StudentTaskStat> stats = new ArrayList<>();

    private final Map<Integer, Task> taskMap = new HashMap<>();

    public XLSXParser(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        parseStudents(sheet);
        parseTasks(sheet);
        parseStudentTaskScore(sheet);

        wb.close();
        fis.close();
    }

    private void parseTasks(Sheet sheet) {
        short lastCellNum = sheet.getRow(1).getLastCellNum();
        for (int i = 12; i < lastCellNum; i++) {
            String fullName = sheet.getRow(1).getCell(i).getStringCellValue();
            if (INVALID_TASK_NAMES.contains(fullName)) continue;
            String[] nameParts = fullName.split(": ");

            Task task = Task.builder()
                    .type(nameParts[0])
                    .name(nameParts[nameParts.length == 1 ? 0 : 1])
                    .maxScore((int) sheet.getRow(2).getCell(i).getNumericCellValue())
                    .theme(parseAndGetTheme(sheet, i))
                    .build();
            tasks.add(task);
            taskMap.put(i, task);
        }
    }

    private Theme parseAndGetTheme(Sheet sheet, int x) {
        String themeName;
        while (sheet.getRow(0).getCell(x).getCellType() == CellType._NONE) x--;
        themeName = sheet.getRow(0).getCell(x).getStringCellValue();

        for (Theme t : themes)
            if (t.getName().equals(themeName)) return t;

        Theme theme = Theme.builder().name(themeName).build();
        themes.add(theme);
        return theme;
    }

    private void parseStudents(Sheet sheet) {
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String[] nameParts = student_parseName(row.getCell(0));
            students.add(Student.builder()
                    .firstname(nameParts[1])
                    .lastname(nameParts[0])
                    .ulearnId(row.getCell(1).getStringCellValue())
                    .email(row.getCell(2).getStringCellValue())
                    .learnGroup(getOrDef(row.getCell(3)))
                    .build()
            );
        }
    }

    private void parseStudentTaskScore(Sheet sheet) {
        int lri = sheet.getLastRowNum();
        int lci = sheet.getRow(3).getLastCellNum();
        for (int y = 3; y <= lri; y++) {
            for (int x = 12; x <= lci; x++) {
                if (!taskMap.containsKey(x)) continue;
                stats.add(StudentTaskStat
                        .builder()
                        .score((int) sheet.getRow(y).getCell(x).getNumericCellValue())
                        .student(students.get(y - 3))
                        .task(taskMap.get(x))
                        .build()
                );
            }
        }
    }

    private String getOrDef(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "N/A";
    }

    private String[] student_parseName(Cell cell) {
        String[] parts = cell.getStringCellValue().split(" ");
        int l = parts.length;
        if (l <= 2) return new String[]{parts[l == 1 ? 0 : 1], parts[0]};

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < l; i++) builder.append(parts[i]).append(" ");
        builder.setLength(builder.length() - 1);
        return new String[]{parts[0], builder.toString()};
    }
}