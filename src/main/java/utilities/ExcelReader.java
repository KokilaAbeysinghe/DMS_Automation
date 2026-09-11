package utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ExcelReader {

    private static final Map<String, String> data = new HashMap<>();

    static {
        String path = "test_data/credentials.xlsx";

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                throw new RuntimeException("Sheet named 'Sheet1' not found in " + path);
            }

            DataFormatter formatter = new DataFormatter(); // reads any cell type as text safely

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip the header row
                }

                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);

                if (keyCell == null || valueCell == null) {
                    continue; // skip blank rows
                }

                String key = formatter.formatCellValue(keyCell).trim();
                String value = formatter.formatCellValue(valueCell).trim();

                if (!key.isEmpty()) {
                    data.put(key, value);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read " + path, e);
        }
    }

    public static String get(String key) {
        String value = data.get(key);
        if (value == null) {
            throw new RuntimeException("No value found in credentials.xlsx for key: " + key);
        }
        return value;
    }
}
