package Medicine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

public class Prescription extends Medicine {
    private String prescriptionID;
    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private LocalDate datePrescribed;
    private Map<String, Integer> medications; // To store medicine and its dosage
    private String status;
    private String notes;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Default constructor
    public Prescription() {}

    // Constructor matching the CSV structure
    public Prescription(String prescriptionID, String patientID, String patientName, String doctorID, 
                        String doctorName, LocalDate datePrescribed, Map<String, Integer> medications, 
                        String status, String notes) {
        this.prescriptionID = prescriptionID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.datePrescribed = datePrescribed;
        this.medications = medications;
        this.status = status;
        this.notes = notes;
    }

    // Getters
    public String getPrescriptionID() {
        return prescriptionID;
    }
    public String getPatientID() {
        return patientID;
    }
    public String getPatientName() {
        return patientName;
    }
    public String getDoctorID() {
        return doctorID;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public LocalDate getDatePrescribed() {
        return datePrescribed;
    }
    public Map<String, Integer> getMedications() {
        return medications;
    }
    public String getStatus() {
        return status;
    }
    public String getNotes() {
        return notes;
    }

    // Setters
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public void setDatePrescribed(LocalDate datePrescribed) {
        this.datePrescribed = datePrescribed;
    }
    public void setMedications(Map<String, Integer> medications) {
        this.medications = medications;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Converts medication map to a CSV-compatible string format (e.g., "Paracetamol:400; Amoxicillin:200")
    public String medicationsToCSV() {
        StringBuilder medicationsCSV = new StringBuilder();
        for (Map.Entry<String, Integer> entry : medications.entrySet()) {
            if (medicationsCSV.length() > 0) {
                medicationsCSV.append("; ");
            }
            medicationsCSV.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return medicationsCSV.toString();
    }

    // Converts this Prescription to a CSV line format
    public String toCSV() {
        return String.join(",", prescriptionID, patientID, patientName, doctorID, doctorName, 
                            datePrescribed.toString(), medicationsToCSV(), status, notes);
    }

    // Parses a CSV line and returns a Prescription object
    public static Prescription fromCSV(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length < 9) {
            System.out.println("Invalid data line: " + csvLine);
            return null;
        }

        String prescriptionID = data[0].trim();
        String patientID = data[1].trim();
        String patientName = data[2].trim();
        String doctorID = data[3].trim();
        String doctorName = data[4].trim();
        LocalDate datePrescribed = LocalDate.parse(data[5].trim(), DATE_FORMAT);
        
        // Parse medications
        Map<String, Integer> medications = parseMedications(data[6].trim());
        
        String status = data[7].trim();
        String notes = data[8].trim();

        return new Prescription(prescriptionID, patientID, patientName, doctorID, doctorName, datePrescribed, medications, status, notes);
    }

    // Helper method to parse medications from a string format to a Map (e.g., "Paracetamol:400; Amoxicillin:200")
    private static Map<String, Integer> parseMedications(String medicationsStr) {
        Map<String, Integer> medicationsMap = new HashMap<>();
        String[] medicationsArray = medicationsStr.split(";");
        
        for (String medication : medicationsArray) {
            String[] parts = medication.trim().split(":");
            if (parts.length == 2) {
                String medName = parts[0].trim();
                int dosage = Integer.parseInt(parts[1].trim());
                medicationsMap.put(medName, dosage);
            }
        }
        
        return medicationsMap;
    }
    public void updateMedicationAmount(String medicationName, int newAmount) {
        if (medications.containsKey(medicationName)) {
            medications.put(medicationName, newAmount);
        } else {
            System.out.println("Medication not found: " + medicationName);
        }
    }
}
