package Patients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages patient data by reading information from a CSV file.
 */
public class PatientData {
    private static final String FILE_PATH = "Patient List CSV.csv";
    private List<Patient> patientList;

    /**
     * Retrieves a list of all patients from the CSV file.
     *
     * @return a list of all patients in the system
     */
    public List<Patient> getAllPatients() {
        patientList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header row
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length < 9) { // Check if line has all expected fields
                    System.out.println("Skipping incomplete line: " + line);
                    continue;
                }
                
                try {
                    // Create a new Patient object with parsed data
                    Patient patient = new Patient(
                        data[0].trim(), // Patient ID
                        data[1].trim(), // Name
                        data[2].trim(), // Date of Birth
                        data[3].trim(), // Gender
                        data[4].trim(), // Blood Type
                        data[5].trim(), // Email
                        data[6].trim(), // Contact Number
                        data[7].trim(), // Emergency Contact
                        data[8].trim()  // Password
                    );
                    
                    patientList.add(patient); // Add patient to list
                    
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading patient data from CSV file.");
            e.printStackTrace();
        }
        return patientList;
    }

    /**
     * Retrieves a patient by their unique ID.
     *
     * @param ID the ID of the patient to search for
     * @return the Patient object if found, otherwise null
     */
    public Patient getPatientByID(String ID) {
        patientList = getAllPatients();
        for (Patient patient : patientList) {
            if (patient.getUserID().equals(ID)) {
                return patient;
            }
        }
        return null; // Patient not found
    }

    /**
     * Checks if a patient with the specified ID exists.
     *
     * @param ID the ID of the patient to search for
     * @return true if the patient exists, false otherwise
     */
    public boolean exists(String ID) {
        return getPatientByID(ID) != null;
    }
}
