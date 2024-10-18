package Login;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//GPT to test importing first

public class AuthenticationService {
    private static final String FILE_PATH = "path_to_your_excel_file.xlsx";  // Change to the actual file path

    public boolean authenticate(String userID, String password) {
        try {
            // Load the Excel file
            FileInputStream file = new FileInputStream(FILE_PATH);
            Workbook workbook = new XSSFWorkbook(file);  // Use XSSFWorkbook for .xlsx files
            Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
            
            // Iterate over each row in the sheet
            for (Row row : sheet) {
                Cell patientIdCell = row.getCell(0);  // Assuming Patient ID is in the first column (A)
                Cell emailCell = row.getCell(6);      // Assuming email is in the 7th column (G)

                if (patientIdCell != null && emailCell != null) {
                    String excelUserID = patientIdCell.getStringCellValue();  // Read Patient ID
                    String excelEmail = emailCell.getStringCellValue();       // Read Password (assuming Contact Information field contains email)
                    
                    // Check if the provided userID and password match the Excel file
                    if (excelUserID.equals(userID) && excelEmail.equals(password)) {
                        workbook.close();
                        return true;  // Authentication successful
                    }
                }
            }

            // Close the workbook and file stream
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;  // Authentication failed if no match found
    }
}
