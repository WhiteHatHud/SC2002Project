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

/**
 * The {@code UpdateRecord} class provides functionalities for updating a patient's medical record.
 * It allows a doctor to modify diagnosis descriptions, prescriptions, treatment dates, outcomes, 
 * follow-up instructions, and notes for a specific patient under their care.
 */
public class UpdateRecord {

    private final String doctorID;
    private final String DIAGNOSIS_CSV_FILE_PATH = "Diagnosis.csv";
    private final String PRESCRIPTION_CSV_FILE_PATH = "To be prescribed.csv";

    /**
     * Constructs an {@code UpdateRecord} instance with the specified doctor ID.
     *
     * @param doctorID the ID of the doctor updating the record
     */
    public UpdateRecord(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Allows a doctor to update an existing medical record for a specific patient.
     * This method prompts the doctor for various updates, including diagnosis description,
     * prescription, treatment dates, outcome, follow-up instructions, and notes.
     */
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
        
        // Prompt doctor to select the record to update or return to the menu
        int selectedRecordIndex = -1;
        while (selectedRecordIndex < 0 || selectedRecordIndex >= matchingRecords.size()) {
            System.out.print("Select the record number you want to update (or press ~ to return to the menu): ");
            String input = scanner.nextLine().trim();

            if (input.equals("~")) {
                System.out.println("Returning to the main menu...");
                return;
            }

            try {
                selectedRecordIndex = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a valid record number.");
            }
        }

        // Update the selected record's details
        String[] selectedRecord = matchingRecords.get(selectedRecordIndex);
        String[] updatedDetails = new String[6];

        // Collect updated information from the doctor
        System.out.print("Enter new Diagnosis Description (leave blank to keep current): ");
        String diagnosisDescription = scanner.nextLine().trim();
        updatedDetails[0] = diagnosisDescription.isEmpty() ? null : diagnosisDescription;

        // Handle prescription updates
        MedicineData medicineData = new MedicineData();
        List<Medicine> availableMedicines = medicineData.getAllMedicines();
        List<String> prescriptions = new ArrayList<>();
        System.out.print("Select medicines for Prescription by entering their numbers (comma-separated): ");
        String[] medicineChoices = scanner.nextLine().trim().split(",");
        
        try {
            for (String choice : medicineChoices) {
                int medicineIndex = Integer.parseInt(choice.trim()) - 1;
                if (medicineIndex < 0 || medicineIndex >= availableMedicines.size()) {
                    System.out.println("Invalid selection: " + choice.trim() + ". Record update aborted.");
                    return;
                }

                String medicineName = availableMedicines.get(medicineIndex).getMedicineName();
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

                prescriptions.add(medicineName + ":" + dosage);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Record update aborted.");
            return;
        }
        
        updatedDetails[1] = String.join("; ", prescriptions);

        // Collect other updated information
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

        // Update the selected record in the CSV
        AdminShared.getCSVUtilities().updateDiagnosisRecordByID(selectedRecord[0], updatedDetails[0], updatedDetails[1],
                updatedDetails[2], updatedDetails[3], updatedDetails[4], updatedDetails[5]);
        
        // Save a new prescription record if a prescription was updated
        if (updatedDetails[1] != null) {
            String prescriptionID = generatePrescriptionID();
            createPrescriptionRecord(prescriptionID, patientID, selectedRecord[2], doctorID,
                    AdminShared.getCSVUtilities().getDoctorNameByID(doctorID), selectedRecord[4], updatedDetails[1], notes);
            System.out.println("New prescription record created successfully for Patient ID: " + patientID);
        }
        
        System.out.println("Record updated successfully for Patient ID: " + patientID);
    }

    /**
     * Validates the format of the prescription string.
     *
     * @param prescription the prescription string to validate
     * @return true if the format is valid; false otherwise
     */
    private boolean isValidPrescriptionFormat(String prescription) {
        String[] medicines = prescription.split(";");
        for (String medicine : medicines) {
            if (!medicine.trim().matches("[A-Za-z]+:\\d+")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates and saves a prescription record to the prescription CSV file.
     *
     * @param pID              the prescription ID
     * @param patientID        the patient ID
     * @param patientName      the patient name
     * @param doctorID         the doctor ID
     * @param doctorName       the doctor name
     * @param datePrescribed   the date prescribed
     * @param medications      the medications prescribed
     * @param notes2           any notes related to the prescription
     */

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


