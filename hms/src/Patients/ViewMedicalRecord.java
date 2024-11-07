package Patients;
import Login.DisplayManager;
import java.util.*;

public class ViewMedicalRecord {

    public void viewMedicalRecord(Patient patient) {
        System.out.println("=== Medical Record ===");
        System.out.println("Patient ID       : " + patient.getUserID());
        System.out.println("Name             : " + patient.getName());
        System.out.println("Date of Birth    : " + patient.getDateOfBirth());
        System.out.println("Gender           : " + patient.getGender());
        System.out.println("Contact Info     : " + patient.getEmail());
        System.out.println("Blood Type       : " + patient.getBloodType());
        System.out.println("Phone Number     : " + patient.getNumber());
        System.out.println("Emergency Contact: " + patient.getenumber());
        System.out.println("=======================");     

        List<String[]> diagnosisData = PatientShared.getCSVUtilities2().getDiagnosisAndPrescriptionByPatientID(patient.getUserID());

        if (diagnosisData.isEmpty()) {
            System.out.println("No Diagnosis found for PatientID: " + patient.getUserID());
        } else {
            System.out.println("Diagnosis and Prescription for PatientID: " + patient.getUserID());
            System.out.println("-------------------------");
            for (String[] details : diagnosisData) {
                System.out.println("Diagnosis Description: " + details[0]);
                System.out.println("Prescription(mg): " + details[1]);
                System.out.println("Date of Diagnosis: " + details[2]);
                System.out.println("-------------------------");
            }
        }

        DisplayManager.retMainMenuAny();
        PatientShared.getUserInputHandler().getNextLine();
        DisplayManager.clearScreen();

    }
}
