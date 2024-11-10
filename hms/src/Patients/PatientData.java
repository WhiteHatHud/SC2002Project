package Patients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientData {
    Patient patient;
    private static final String FILE_PATH = "Patient List CSV.csv";
    List<Patient> patientList;

    public List<Patient> getAllPatients(){
        patientList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine();
            // Skip header row
            while ((line = reader.readLine()) != null) {
                // System.out.println("Reading line: " + line); // Debugging print statement
                String[] data = line.split(",");
                if (data.length < 7) {
                    //System.out.println("Skipping incomplete line: " + line); // Debugging print for skipped lines
                    continue;
                }
                try {
                    patient = new Patient(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim(), data[8].trim());
                    patientList.add(patient);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    public Patient getPatientByID(String ID){
        patientList = getAllPatients();
        for (Patient patients : patientList){
            if(patients.getUserID().equals(ID)){
                return patients;
            }
        }
        return null;
    }

    public boolean exists(String ID){
        if (getPatientByID(ID) != null){
            return true;
        }
        return false;
    }
}
