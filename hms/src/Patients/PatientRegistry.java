package Patients;

import Login.UserRegistry;
import java.util.ArrayList;
import java.util.List;

public class PatientRegistry implements UserRegistry<Patient> {

    private List<Patient> patients = new ArrayList<>();

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
        // Method to print each patient in the registry
        public void printAllPatients() {
            // Get the list of patients from the registry
            for (Patient patient : getAllUsers()) {
                // Print patient details
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
