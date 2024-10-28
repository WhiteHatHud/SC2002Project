package Patients;

public class ViewMedicalRecord {
    

    public void viewMedicalRecord(Patient patient) {
        System.out.println("=== Medical Record ===");
        System.out.println("Patient ID       : " + patient.getUserID());
        System.out.println("Name             : " + patient.getName());
        System.out.println("Date of Birth    : " + patient.getDateOfBirth());
        System.out.println("Gender           : " + patient.getGender());
        System.out.println("Contact Info     : " + patient.getEmail());
        System.out.println("Blood Type       : " + patient.getBloodType());
        
        System.out.println("=======================");
    }
}
