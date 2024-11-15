package Patients;

import Login.UserRegistry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The PatientRegistry class manages a collection of Patient objects, allowing
 * for operations such as adding, finding, deleting, and updating patient data.
 * It can also reload the registry from a CSV file.
 */
public class PatientRegistry implements UserRegistry<Patient> {

    private List<Patient> patients = new ArrayList<>();
    private final String patientCSVFile = "Patient List CSV.csv"; // Ensure this path is correct

    /**
     * Adds a Patient object to the registry.
     * 
     * @param patient The Patient object to add to the registry.
     */
    @Override
    public void addUser(Patient patient) {
        patients.add(patient);
    }

    /**
     * Retrieves a list of all patients in the registry.
     * 
     * @return A list of all Patient objects.
     */
    @Override
    public List<Patient> getAllUsers() {
        return patients;
    }

    /**
     * Finds a patient by their unique ID.
     * 
     * @param userID The unique ID of the patient to find.
     * @return The Patient object if found, or null if not found.
     */
    @Override
    public Patient findUserByID(String userID) {
        return patients.stream()
                       .filter(patient -> patient.getUserID().equals(userID))
                       .findFirst()
                       .orElse(null);
    }

    /**
     * Reloads the patient registry by reading from the CSV file.
     * This method replaces the current list of patients with the refreshed list
     * loaded from the file.
     */
    public void reloadRegistry() {
        List<Patient> refreshedPatients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(patientCSVFile))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header line
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 8) { // Ensure there are enough columns
                    Patient patient = new Patient(
                            data[0].trim(),  // Patient ID
                            data[1].trim(),  // Name
                            data[2].trim(),  // Date of Birth
                            data[3].trim(),  // Gender
                            data[4].trim(),  // Blood Type
                            data[5].trim(),  // Email
                            data[6].trim(),  // Contact Number
                            data[7].trim(),  // Emergency Contact Number
                            data[8].trim()   // Password
                    );
                    refreshedPatients.add(patient);
                }
            }
            this.patients = refreshedPatients;
            System.out.println("Patient registry reloaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reloading patient registry: " + e.getMessage());
        }
    }

    /**
     * Deletes a patient by their unique ID.
     * 
     * @param userID The unique ID of the patient to delete.
     * @return true if the patient was found and removed, false if not found.
     */
    public boolean deletePatientByID(String userID) {
        Patient patientToRemove = findUserByID(userID);
        if (patientToRemove != null) {
            patients.remove(patientToRemove);
            System.out.println("Patient with ID " + userID + " has been removed.");
            return true;
        } else {
            System.out.println("Patient with ID " + userID + " not found.");
            return false;
        }
    }

    /**
     * Updates the password of a patient identified by their unique ID.
     * 
     * @param userID The unique ID of the patient whose password is to be updated.
     * @param newPassword The new password to set for the patient.
     * @return true if the password was successfully updated, false if the patient
     *         was not found.
     */
    public boolean updatePatientPassword(String userID, String newPassword) {
        Patient patient = findUserByID(userID);
        if (patient != null) {
            patient.setPassword(newPassword);
            System.out.println("Password updated successfully for patient ID " + userID + ".");
            return true;
        } else {
            System.out.println("Patient with ID " + userID + " not found.");
            return false;
        }
    }

    /**
     * Prints the details of all patients in the registry.
     */
    public void printAllPatients() {
        for (Patient patient : getAllUsers()) {
            System.out.println("Patient ID: " + patient.getUserID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + patient.getDateOfBirth());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Blood Type: " + patient.getBloodType());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("--------------------------");
        }
        System.out.println("DONE!");
    }
}
