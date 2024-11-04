package Doctors;

import Medicine.Medicine;
import Medicine.MedicineData;
import Medicine.MedicineUI;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateRecord {
    private final String doctorID;
    private final Scanner scanner = new Scanner(System.in);
    private final String CSV_FILE_PATH = "Diagnosis.csv"; // Change the file name as needed

    public CreateRecord(String doctorID) {
        this.doctorID = doctorID;
    }

    public void createRecord() {
        System.out.println("=== Create Patient Diagnosis and Treatment Record ===");

        // Retrieve Patient ID and Name
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine().trim();

        String patientName = retrievePatientName(patientID);
        if (patientName == null) {
            System.out.println("Invalid Patient ID. Record creation aborted.");
            return;
        }
        System.out.println("Patient Name: " + patientName);

        // Automatically use today's date for DiagnosisDate
        LocalDate diagnosisDate = LocalDate.now();
        System.out.println("Diagnosis Date (default is today, " + diagnosisDate + ")");
        System.out.print("Do you want to use a different date? (yes/no): ");
        String changeDate = scanner.nextLine().trim().toLowerCase();
        if (changeDate.equals("yes")) {
            System.out.print("Enter Diagnosis Date (YYYY-MM-DD): ");
            diagnosisDate = LocalDate.parse(scanner.nextLine().trim());
        }

        // Diagnosis Description
        System.out.print("Enter Diagnosis Description: ");
        String diagnosisDescription = scanner.nextLine().trim();

        // Prescription selection from available medicines
        MedicineUI medicineUI = new MedicineUI();
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
                prescriptions.add(availableMedicines.get(medicineIndex).getMedicineName());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Record creation aborted.");
            return;
        }

        // Join selected medicines for display, separated by ";"
        String prescription = String.join("; ", prescriptions);

        // Treatment Start and End Dates
        System.out.print("Enter Treatment Start Date (YYYY-MM-DD): ");
        LocalDate treatmentStartDate = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Enter Treatment End Date (YYYY-MM-DD, if applicable): ");
        String treatmentEndDateInput = scanner.nextLine().trim();
        LocalDate treatmentEndDate = treatmentEndDateInput.isEmpty() ? null : LocalDate.parse(treatmentEndDateInput);

        
        String treatmentOutcome = null;
        String followUpInstructions = null;

        // Display summary and confirm record creation
        System.out.println("\n--- Confirm Record ---");
        System.out.printf("Patient ID: %s\nPatient Name: %s\nDoctor ID: %s\nDiagnosis Date: %s\n" +
                        "Diagnosis Description: %s\nPrescription: %s\nTreatment Start Date: %s\n" +
                        "Treatment End Date: %s\nTreatment Outcome: %s\nFollow-Up Instructions: %s\n",
                patientID, patientName, doctorID, diagnosisDate, diagnosisDescription, prescription,
                treatmentStartDate, treatmentEndDate, treatmentOutcome, followUpInstructions);

        System.out.print("Save this record? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            saveRecordToCSV(patientID, patientName, doctorID, diagnosisDate, diagnosisDescription,
                            prescription, treatmentStartDate, treatmentEndDate, treatmentOutcome, followUpInstructions);
        } else {
            System.out.println("Record creation canceled.");
        }
    }

    private String retrievePatientName(String patientID) {
        return DoctorShared.getcsvUtilities().getPatientNameByID(patientID);
    }

    private void saveRecordToCSV(String patientID, String patientName, String doctorID, LocalDate diagnosisDate,
                                 String diagnosisDescription, String prescription, LocalDate treatmentStartDate,
                                 LocalDate treatmentEndDate, String treatmentOutcome, String followUpInstructions) {

        // Generate a new unique ID for the record
        int newID = getNextID();

        String treatmentEndDateStr = (treatmentEndDate != null) ? treatmentEndDate.toString() : "";
        String record = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                newID,
                patientID,
                patientName,
                doctorID,
                diagnosisDate,
                diagnosisDescription,
                prescription,  // Now using semicolon-separated list of prescriptions
                treatmentStartDate,
                treatmentEndDateStr,
                treatmentOutcome,
                followUpInstructions,
                "");  // Notes field left empty

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            // If file does not exist or is empty, add headers
            if (isFileEmpty(CSV_FILE_PATH)) {
                writer.write("ID,PatientID,PatientName,DoctorID,DiagnosisDate,DiagnosisDescription,Prescription," +
                        "TreatmentStartDate,TreatmentEndDate,TreatmentOutcome,FollowUpInstructions,Notes");
                writer.newLine();
            }
            writer.write(record);
            writer.newLine();
            System.out.println("Record saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private int getNextID() {
        int lastID = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE_PATH));
            if (lines.size() > 1) {  // Ignore the header line
                String lastLine = lines.get(lines.size() - 1);
                String[] values = lastLine.split(",");
                lastID = Integer.parseInt(values[0]);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading CSV for last ID: " + e.getMessage());
        }
        return lastID + 1;
    }

    // Helper method to check if file exists and is empty
    private boolean isFileEmpty(String filePath) {
        File file = new File(filePath);
        return !file.exists() || file.length() == 0;
    }
}
