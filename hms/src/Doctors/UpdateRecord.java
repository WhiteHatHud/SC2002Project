package Doctors;

import Admins.AdminShared;
import Medicine.Medicine;
import Medicine.MedicineData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UpdateRecord {
    private final String doctorID;
    private final String DIAGNOSIS_CSV_FILE_PATH = "Diagnosis.csv";
    private final String PRESCRIPTION_CSV_FILE_PATH = "To be prescribed.csv";

    public UpdateRecord(String doctorID) {
        this.doctorID = doctorID;
    }

    public void updateRecord() {
        Scanner scanner = new Scanner(System.in);
        
        // Ask for the Patient ID
        System.out.print("Enter Patient ID to update record: ");
        String patientID = scanner.nextLine().trim();
        
        // Retrieve all records for this patient under the doctor's care
        List<String[]> matchingRecords = AdminShared.getCSVUtilities().getRecordsForPatientUnderDoctor(patientID, doctorID);
        
        if (matchingRecords.isEmpty()) {
            System.out.println("No record found for Patient ID " + patientID + " under your care.");
            return;
        }
        
        // Display options for multiple records and select one
        System.out.println("Found multiple records for Patient ID: " + patientID);
        for (int i = 0; i < matchingRecords.size(); i++) {
            String[] record = matchingRecords.get(i);
            System.out.printf("%d. Diagnosis ID: %s, Diagnosis Date: %s, Description: %s%n", 
                              i + 1, record[0], record[4], record[5]);
        }
        
        int selectedRecordIndex = -1;
        while (selectedRecordIndex < 0 || selectedRecordIndex >= matchingRecords.size()) {
            System.out.print("Select the record number you want to update (or press ~ to return to the menu): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("~")) {
                System.out.println("Returning to the main menu...");
                return; // Exit the method to return to the menu
            }
            
            try {
                selectedRecordIndex = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a valid record number.");
            }
        }
        
    
        // Get the selected record
        String[] selectedRecord = matchingRecords.get(selectedRecordIndex);
    
        // Display current information
        System.out.println("Record found for Patient ID: " + patientID);
        System.out.println("Current Diagnosis Description: " + selectedRecord[5]);
        System.out.println("Current Prescription: " + selectedRecord[6]);
        System.out.println("Current Treatment End Date: " + selectedRecord[8]);
        System.out.println("Current Treatment Outcome: " + selectedRecord[9]);
        System.out.println("Current Follow-Up Instructions: " + selectedRecord[10]);
        System.out.println("Current Notes: " + selectedRecord[11]);
    
        String[] updatedDetails = new String[6]; // Array to hold updates in the order of fields
    
        // Gather updated information
        System.out.print("Enter new Diagnosis Description (leave blank to keep current): ");
        String diagnosisDescription = scanner.nextLine().trim();
        updatedDetails[0] = diagnosisDescription.isEmpty() ? null : diagnosisDescription;
    
        // Prescription selection from available medicines
        MedicineData medicineData = new MedicineData();
        List<Medicine> availableMedicines = medicineData.getAllMedicines();
    
        System.out.println("Available Medicines:");
        for (int i = 0; i < availableMedicines.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, availableMedicines.get(i).getMedicineName());
        }
    
        System.out.print("Select medicines for Prescription by entering their numbers (comma-separated): ");
        String[] medicineChoices = scanner.nextLine().trim().split(",");
        List<String> prescriptions = new ArrayList<>();
    
        try {
            for (String choice : medicineChoices) {
                int medicineIndex = Integer.parseInt(choice.trim()) - 1;
                if (medicineIndex < 0 || medicineIndex >= availableMedicines.size()) {
                    System.out.println("Invalid selection: " + choice.trim() + ". Record update aborted.");
                    return;
                }
    
                // Get medicine name
                String medicineName = availableMedicines.get(medicineIndex).getMedicineName();
    
                // Prompt for dosage and validate
                int dosage;
                while (true) {
                    System.out.print("Enter dosage for " + medicineName + " (increments of 100): ");
                    dosage = Integer.parseInt(scanner.nextLine().trim());
    
                    // Check if dosage is in increments of 100
                    if (dosage % 100 == 0) {
                        break; // Exit loop if dosage is valid
                    } else {
                        System.out.println("Dosage must be in increments of 100. Please try again.");
                    }
                }
    
                // Add medicine and dosage to prescription list
                prescriptions.add(medicineName + ":" + dosage);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Record update aborted.");
            return;
        }
    
        // Join selected medicines for storage, separated by ";"
        String prescription = String.join("; ", prescriptions);
        updatedDetails[1] = prescription;
    
        System.out.print("Enter new Treatment End Date (YYYY-MM-DD, leave blank to keep current): ");
        String treatmentEndDate = scanner.nextLine().trim();
        updatedDetails[2] = treatmentEndDate.isEmpty() ? null : treatmentEndDate;
    
        System.out.print("Enter new Treatment Outcome (leave blank to keep current): ");
        String treatmentOutcome = scanner.nextLine().trim();
        updatedDetails[3] = treatmentOutcome.isEmpty() ? null : treatmentOutcome;
    
        System.out.print("Enter new Follow-Up Instructions (leave blank to keep current): ");
        String followUpInstructions = scanner.nextLine().trim();
        updatedDetails[4] = followUpInstructions.isEmpty() ? null : followUpInstructions;
    
        System.out.print("Enter new Notes: ");
        String notes = scanner.nextLine().trim();
        updatedDetails[5] = notes.isEmpty() ? null : notes;
    
        // Update the specific selected record
        AdminShared.getCSVUtilities().updateDiagnosisRecordByID(selectedRecord[0], updatedDetails[0], updatedDetails[1],
            updatedDetails[2], updatedDetails[3], updatedDetails[4], updatedDetails[5]);
    
        System.out.println("Record updated successfully for Patient ID: " + patientID);
    
        // Generate a new prescription ID and create a new prescription record
        String prescriptionID = generatePrescriptionID();
        createPrescriptionRecord(prescriptionID, patientID, selectedRecord[2], doctorID, 
            AdminShared.getCSVUtilities().getDoctorNameByID(doctorID), selectedRecord[4], updatedDetails[1], notes);
    
        System.out.println("New prescription record created successfully for Patient ID: " + patientID);
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
     // Method to generate and save a prescription record i
 private void createPrescriptionRecord(String pID,String patientID, String patientName, String doctorID, String doctorName, String datePrescribed, String medications,String notes2) {
    String prescriptionID = pID;
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
}
