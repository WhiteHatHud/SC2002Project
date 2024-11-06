package Doctors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UpdateRecord {
    private final String doctorID;
    private final String DIAGNOSIS_CSV_FILE_PATH = "Diagnosis.csv";

    public UpdateRecord(String doctorID) {
        this.doctorID = doctorID;
    }

    public void updateRecord() {
        Scanner scanner = new Scanner(System.in);
    
        // Prompt for Patient ID to locate record
        System.out.print("Enter Patient ID to update record: ");
        String patientID = scanner.nextLine().trim();
    
        // Load diagnosis records from file
        List<String[]> records = loadRecords();
        boolean found = false;
    
        for (String[] record : records) {
            if (record[1].equals(patientID) && record[3].equals(doctorID)) {
                found = true;
                System.out.println("Record found for Patient ID: " + patientID);
    
                // Ensure the record has enough fields
                record = standardizeRecordLength(record);
    
                System.out.println("Diagnosis Description: " + record[5]);
                System.out.println("Prescription: " + record[6]);
                System.out.println("Treatment End Date: " + record[8]);
                System.out.println("Treatment Outcome: " + record[9]);
                System.out.println("Follow-Up Instructions: " + record[10]);
    
                // Allow updates
                System.out.print("Enter new Diagnosis Description (leave blank to keep current): ");
                String diagnosisDescription = scanner.nextLine().trim();
                if (!diagnosisDescription.isEmpty()) {
                    record[5] = diagnosisDescription;
                }
    
                String prescription;
                while (true) {
                    System.out.print("Enter new Prescription (format: Medicine:Dosage; Medicine2:Dosage2;...): ");
                    prescription = scanner.nextLine().trim();
                    if (prescription.isEmpty() || isValidPrescriptionFormat(prescription)) {
                        if (!prescription.isEmpty()) {
                            record[6] = prescription;
                        }
                        break;
                    } else {
                        System.out.println("Invalid format. Please use format: Medicine1:dosage; Medicine2:dosage; etc.");
                    }
                }
    
                System.out.print("Enter new Treatment End Date (YYYY-MM-DD, leave blank to keep current): ");
                String treatmentEndDate = scanner.nextLine().trim();
                if (!treatmentEndDate.isEmpty()) {
                    if (validateDate(treatmentEndDate)) {
                        record[8] = treatmentEndDate;
                    } else {
                        System.out.println("Invalid date format. Keeping current Treatment End Date.");
                    }
                }
    
                System.out.print("Enter new Treatment Outcome (leave blank to keep current): ");
                String treatmentOutcome = scanner.nextLine().trim();
                if (!treatmentOutcome.isEmpty()) {
                    record[9] = treatmentOutcome;
                }
    
                System.out.print("Enter new Follow-Up Instructions (leave blank to keep current): ");
                String followUpInstructions = scanner.nextLine().trim();
                if (!followUpInstructions.isEmpty()) {
                    record[10] = followUpInstructions;
                }
    
                // Save changes back to the file
                saveRecords(records);
                System.out.println("Record updated successfully for Patient ID: " + patientID);
                break;
            }
        }
    
        if (!found) {
            System.out.println("No records found for Patient ID " + patientID + " under your care.");
        }
    }

    
    // Helper method to validate date format
    private boolean validateDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}"); // Simple regex to match YYYY-MM-DD format
    }

    
    
    // Helper method to standardize the length of each record array
    private String[] standardizeRecordLength(String[] record) {
        String[] standardizedRecord = new String[11]; // Adjust based on the number of fields
        for (int i = 0; i < standardizedRecord.length; i++) {
            standardizedRecord[i] = (i < record.length) ? record[i] : ""; // Use existing value or set to empty
        }
        return standardizedRecord;
    }

    
    
    // Helper method to expand record array if needed
    private void addOrExpandRecord(String[] record, int index, String value) {
        // Create a new array with a size that includes the required index
        String[] expandedRecord = new String[Math.max(record.length, index + 1)];
        System.arraycopy(record, 0, expandedRecord, 0, record.length);
        expandedRecord[index] = value;
        // Replace original record array with the expanded array
        record = expandedRecord;
    }
    
    // Helper method to validate prescription format
    private boolean isValidPrescriptionFormat(String prescription) {
        String[] medicines = prescription.split(";");
        for (String medicine : medicines) {
            if (!medicine.trim().matches("[A-Za-z]+:\\d+")) {
                return false; // Invalid format if it doesn't match "Medicine:Dosage"
            }
        }
        return true;
    }
    

    private List<String[]> loadRecords() {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DIAGNOSIS_CSV_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header row
            while ((line = reader.readLine()) != null) {
                records.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error reading diagnosis records: " + e.getMessage());
        }
        return records;
    }

    private void saveRecords(List<String[]> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIAGNOSIS_CSV_FILE_PATH))) {
            writer.write("DiagnosisID,PatientID,PatientName,DoctorID,DiagnosisDate,DiagnosisDescription," +
                         "Prescription,TreatmentStartDate,TreatmentEndDate,TreatmentOutcome,FollowUpInstructions,Notes");
            writer.newLine();
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving updated diagnosis records: " + e.getMessage());
        }
    }
}
