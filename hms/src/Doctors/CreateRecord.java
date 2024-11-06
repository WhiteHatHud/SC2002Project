package Doctors;

import Medicine.Medicine;
import Medicine.MedicineData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateRecord {
    private final String doctorID;
    private final Scanner scanner = new Scanner(System.in);
    private final String PRESCRIPTION_CSV_FILE_PATH = "To be prescribed.csv"; // Change the file name as needed

    public CreateRecord(String doctorID) {
        this.doctorID = doctorID;
    }

    public void createRecord() {
        System.out.println("=== Create Patient Diagnosis and Treatment Record ===");
        
        // Retrieve Patient ID and Name
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine().trim();
        
        String patientName = DoctorShared.getcsvUtilities().getPatientNameByID(patientID);
        if (patientName == null) {
            System.out.println("Invalid Patient ID. Record creation aborted.");
            return;
        }
        System.out.println("Patient Name: " + patientName);
        
        String diagnosisID = generateDiagnosisID();
        LocalDate diagnosisDate = LocalDate.now();
        
        System.out.println("Diagnosis Date (default is today, " + diagnosisDate + ")");
        System.out.print("Do you want to use a different date? (yes/no): ");
        String changeDate = scanner.nextLine().trim().toLowerCase();
        if (changeDate.equals("yes")) {
            System.out.print("Enter Diagnosis Date (YYYY-MM-DD): ");
            try {
                diagnosisDate = LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using default date.");
            }
        }
    
        // Diagnosis Description
        System.out.print("Enter Diagnosis Description: ");
        String diagnosisDescription = scanner.nextLine().trim();
        
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
                    System.out.println("Invalid selection: " + choice.trim() + ". Record creation aborted.");
                    return;
                }
    
                String medicineName = availableMedicines.get(medicineIndex).getMedicineName();
                
                // Prompt for dosage and validate
                int dosage;
                while (true) {
                    System.out.print("Enter dosage for " + medicineName + " (increments of 100): ");
                    dosage = Integer.parseInt(scanner.nextLine().trim());
    
                    if (dosage % 100 == 0) {
                        break;
                    } else {
                        System.out.println("Dosage must be in increments of 100. Please try again.");
                    }
                }
    
                // Add medicine and dosage to prescription list
                prescriptions.add(medicineName + ":" + dosage);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Record creation aborted.");
            return;
        }
        
        // Join selected medicines, separated by ";"
        String prescription = String.join("; ", prescriptions);
    
        System.out.print("Notes for medication: ");
        String notes = scanner.nextLine().trim();
    
        // Treatment Start and End Dates
        LocalDate treatmentStartDate;
        while (true) {
            System.out.print("Enter Treatment Start Date (YYYY-MM-DD): ");
            try {
                treatmentStartDate = LocalDate.parse(scanner.nextLine().trim());
                if (!treatmentStartDate.isBefore(LocalDate.now())) {
                    break;
                } else {
                    System.out.println("Start date must be today or in the future. Please try again.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    
        LocalDate treatmentEndDate;
        while (true) {
            System.out.print("Enter Treatment End Date (YYYY-MM-DD, if applicable): ");
            String treatmentEndDateInput = scanner.nextLine().trim();
            
            if (treatmentEndDateInput.isEmpty()) {
                treatmentEndDate = null;
                break;
            } else {
                try {
                    treatmentEndDate = LocalDate.parse(treatmentEndDateInput);
                    if (treatmentEndDate.isAfter(treatmentStartDate)) {
                        break;
                    } else {
                        System.out.println("End date must be after the start date. Please try again.");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }
        }
        
        String treatmentOutcome = "";
        String followUpInstructions = "";
        
        // Confirm record creation
        System.out.println("\n--- Confirm Record ---");
        System.out.printf("Patient ID: %s\nPatient Name: %s\nDoctor ID: %s\nDiagnosis Date: %s\n" +
                        "Diagnosis Description: %s\nPrescription: %s\nTreatment Start Date: %s\n" +
                        "Treatment End Date: %s\nTreatment Outcome: %s\nFollow-Up Instructions: %s\n",
                patientID, patientName, doctorID, diagnosisDate, diagnosisDescription, prescription,
                treatmentStartDate, treatmentEndDate, treatmentOutcome, followUpInstructions);
    
        System.out.print("Save this record? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
    
        notes = notes.isEmpty() ? "" : notes;  // Ensure empty fields are set to empty strings
    
        if (confirm.equals("yes")) {
            // Ensure each field is non-null or empty
            String[] recordData = {
                diagnosisID,
                patientID != null ? patientID : "",
                patientName != null ? patientName : "",
                doctorID != null ? doctorID : "",
                diagnosisDate != null ? diagnosisDate.toString() : "",
                diagnosisDescription != null ? diagnosisDescription : "",
                prescription != null ? prescription : "",
                treatmentStartDate != null ? treatmentStartDate.toString() : "",
                (treatmentEndDate != null) ? treatmentEndDate.toString() : "",
                treatmentOutcome,
                followUpInstructions,
                notes
            };
        
            String pID= generatePrescriptionID();

            DoctorShared.getCSVUpdater().addNewLineToCSV(recordData, "Diagnosis.csv",12);

            createPrescriptionRecord(pID, patientID, patientName, doctorID, DoctorShared.getcsvUtilities2().getDoctorNameByID(doctorID),
            diagnosisDate.toString(), prescription, notes);
            //System.out.println("Record saved successfully.");
        } else {
            System.out.println("Record creation canceled.");
        }
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