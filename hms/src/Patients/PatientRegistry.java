package Patients;

import Login.UserRegistry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientRegistry implements UserRegistry<Patient> {

    private List<Patient> patients = new ArrayList<>();
    private final String patientCSVFile = "Patient List CSV.csv"; // Ensure this path is correct

    @Override
    public void addUser(Patient patient) {
        patients.add(patient);
    }

    @Override
    public List<Patient> getAllUsers() {
        return patients;
    }

    @Override
    public Patient findUserByID(String userID) {
        return patients.stream()
                       .filter(patient -> patient.getUserID().equals(userID))
                       .findFirst()
                       .orElse(null);
    }

    // Method to reload the registry from the CSV file
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
                            data[8].trim()
                    );
                    refreshedPatients.add(patient);
                }
            }
            // Replace the current list with the refreshed list
            this.patients = refreshedPatients;
            System.out.println("Patient registry reloaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reloading patient registry: " + e.getMessage());
        }
    }

    // Method to delete a patient by ID
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

    // Method to update a patient's password by ID
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

    // Method to print each patient in the registry
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
