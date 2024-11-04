package MedicalHistory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MedicalData {
    private ArrayList<PatientHistory> patients = new ArrayList<>();

    // Method to load patients from CSV file
    public void loadPatientsFromCSV(String filePath) {
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            br.readLine();

            // Read each line from the CSV file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);

                // Extract relevant columns
                String patientID = data[0];
                String name = data[1];
                String dateOfBirth = data[2];
                String gender = data[3];
                String bloodType = data[4];
                String contactInfo = data[5];

                // Create a new PatientHistory object and add it to the list
                PatientHistory patient = new PatientHistory(patientID, name, dateOfBirth, gender, bloodType, contactInfo);
                patients.add(patient);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // Method to get the list of patients
    public ArrayList<PatientHistory> getPatients() {
        return patients;
    }

    // For debugging: Print all patients
    public void printPatients() {
        for (PatientHistory patient : patients) {
            System.out.println(patient);
        }
    }
}
