package Medicine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MedHelper {



 // Method to generate and save a prescription record i
 private void createPrescriptionRecord(String patientID, String patientName, String doctorID, String doctorName, String datePrescribed, String medications,String notes2) {
    String PRESCRIPTION_CSV_FILE_PATH = "To be prescribed.csv";
    String prescriptionID = generatePrescriptionID(); // Generate a unique ID for the prescription
    String status = "Pending"; // Default status
    String notes = notes2;

    String recordLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
            prescriptionID,
            patientID,
            patientName,
            doctorID,
            doctorName,
            datePrescribed,
            medications,
            status,
            notes);


    try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRESCRIPTION_CSV_FILE_PATH, true))) {

        if (Files.size(Paths.get(PRESCRIPTION_CSV_FILE_PATH)) == 0) {
            writer.write("PrescriptionID,PatientID,PatientName,DoctorID,DoctorName,DatePrescribed,Medications(mg),Status,Notes");
            writer.newLine();
        }
        writer.write(recordLine);
        writer.newLine();
        System.out.println("Prescription record saved successfully!");
    } catch (IOException e) {
        System.out.println("Error writing to Prescription CSV file: " + e.getMessage());
    }
}
private String generatePrescriptionID() {
    String PRESCRIPTION_CSV_FILE_PATH = "To be prescribed.csv";

    int lastID = 0;
    try {
        List<String> lines = Files.readAllLines(Paths.get(PRESCRIPTION_CSV_FILE_PATH));
        
        // Check if the file is empty or only contains a header
        if (lines.size() <= 1) {
            return "PR001";  // Start from PR001 if no valid IDs are found
        }
        
        for (int i = lines.size() - 1; i >= 1; i--) {  // Start from the end, skipping the header row
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;  // Skip empty lines

            String[] values = line.split(",");
            if (values.length > 0 && values[0].startsWith("PR")) {  // Check if ID format is "PRXXX"
                try {
                    lastID = Integer.parseInt(values[0].replace("PR", ""));
                    break;  // Stop once we find the latest valid ID
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid ID format: " + values[0]);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading Prescription CSV for last ID: " + e.getMessage());
    }

    return String.format("PR%03d", lastID + 1);  // Increment and format the new ID
}
private String generateDiagnosisID() {
    int lastID = 0;
    String DIAGNOSIS_CSV_FILE_PATH = "Diagnosis.csv"; 

    try {
        List<String> lines = Files.readAllLines(Paths.get(DIAGNOSIS_CSV_FILE_PATH));
        
        // Check if the file is empty or only contains a header
        if (lines.size() <= 1) {
            return "1";  // Start from 1 if no valid IDs are found
        }
        
        // Start from the end, skipping the header row
        for (int i = lines.size() - 1; i >= 1; i--) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue; 

            String[] values = line.split(",");
            if (values.length > 0 && values[0].matches("\\d+")) { 
                try {
                    lastID = Integer.parseInt(values[0]);
                    break; // Stop once we find the latest valid ID
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid ID format: " + values[0]);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading Diagnosis CSV for last ID: " + e.getMessage());
    }

    return Integer.toString(lastID + 1);  
}

    
}
