package Medicine;

import Utilities.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionData {
    private static final String FILE_PATH = "To be prescribed.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private CSVUpdater csvUpdate = new CSVUpdater(FILE_PATH);
    private CSVUtilities csvUtilities = new CSVUtilities(FILE_PATH);
    private List<Prescription> prescriptionList;

    public List<Prescription> getAllPrescriptions() {
        prescriptionList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header row
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 9) continue;

                try {
                    // Parsing each field based on the CSV structure
                    String prescriptionID = data[0].trim();
                    String patientID = data[1].trim();
                    String patientName = data[2].trim();
                    String doctorID = data[3].trim();
                    String doctorName = data[4].trim();
                    LocalDate datePrescribed = LocalDate.parse(data[5].trim(), DATE_FORMAT);
                    Map<String, Integer> medications = parseMedications(data[6].trim());
                    String status = data[7].trim();
                    String notes = data[8].trim();

                    // Creating a new Prescription object with parsed data
                    Prescription pres = new Prescription(prescriptionID, patientID, patientName, doctorID, 
                                                         doctorName, datePrescribed, medications, status, notes);
                    prescriptionList.add(pres);
                } catch (Exception e) {
                    System.out.println("Skipping line due to parsing error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return prescriptionList;
    }

    private Map<String, Integer> parseMedications(String medicationsStr) {
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

    public List<Prescription> getPatientPrescriptionList(String patientID) {
        prescriptionList = getAllPrescriptions();
        prescriptionList.removeIf(n -> !n.getPatientID().equals(patientID));
        return prescriptionList;
    }

    public Prescription getPrescription(String prescriptionID) {
        prescriptionList = getAllPrescriptions();
        for (Prescription pres : prescriptionList) {
            if (pres.getPrescriptionID().equals(prescriptionID)) return pres;
        }
        return null;
    }

    public boolean addPrescription(Prescription prescription) {
        if (exists(prescription.getPatientID(), prescription.getMedicineName())) {
            return false;
        }
        String[] newData = prescription.toCSV().split(",");
        csvUpdate.addNewLine(newData);
        return true;
    }

    public boolean exists(String patientID, String medicineName) {
        prescriptionList = getPatientPrescriptionList(patientID);
        for (Prescription pres : prescriptionList) {
            if (pres.getMedicineName().equals(medicineName)) return true;
        }
        return false;
    }

    public boolean updateAllPrescriptions() {
        List<String[]> updatedData = new ArrayList<>();
        updatedData.add(new String[]{"PrescriptionID", "PatientID", "PatientName", "DoctorID", "DoctorName", 
                                     "DatePrescribed", "Medications", "Status", "Notes"}); // Header

        for (Prescription pres : prescriptionList) {
            updatedData.add(pres.toCSV().split(","));
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (String[] row : updatedData) {
                writer.println(String.join(",", row));
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
        return false;
    }

    public boolean increaseAmount(String prescriptionID, String medicineID, int newAmount) {
        prescriptionList = getAllPrescriptions();
        for (Prescription pres : prescriptionList) {
            if (pres.getPrescriptionID().equals(prescriptionID)) {
                pres.updateMedicationAmount(medicineID, newAmount);
                return updateAllPrescriptions();
            }
        }
        return false;
    }
     

    public boolean updatePrescriptionStatus(String prescriptionID, String newStatus) {
        csvUpdate.updateField(prescriptionID, "status", newStatus);
        return true;
    }

    public boolean deletePrescription(String patientID, String medicineID) {
        prescriptionList = getAllPrescriptions();
        boolean removed = prescriptionList.removeIf(n -> 
            n.getPatientID().equals(patientID) && n.getMedicineName().equals(medicineID)
        );
        if (removed) {
            updateAllPrescriptions();
        }
        return removed;
    }

    public String generatePrescriptionID() {
        String prefix = "PR"; // Prefix for Prescription IDs
        int highestID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].startsWith(prefix)) { 
                    int idNumber = Integer.parseInt(fields[0].substring(2));
                    if (idNumber > highestID) {
                        highestID = idNumber;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int newIDNumber = highestID + 1;
        return prefix + String.format("%03d", newIDNumber); // Returns ID in the format PR001, PR002, etc.
    }

    public boolean checkPrescriptionExists(String prescriptionID){
        return csvUtilities.checkIfUserExists(prescriptionID);
    }

}
