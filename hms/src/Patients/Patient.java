package Patients;
import Users.*;

import Login.Users;

public class Patient extends Users {

    private String dob;
    private String gender;
    private String bloodType;
    private String email;


    public Patient(String patientID, String name, String dob, String gender, String bloodType, String contactInfo) {
        super(patientID, name, dob); 
        this.dob = dob;             
        this.gender = gender;
        this.bloodType = bloodType;
        this.email = contactInfo;
    }
    
    // Getter methods specific to Patient
    public String getDateOfBirth() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public void displayMenu() {
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
    }

    // Method to display patient profile
    public void viewProfile() {
        System.out.println("Patient ID: " + userID + ", Name: " + name + 
                           ", Email: " + email + ", DOB: " + dob + 
                           ", Gender: " + gender + ", Blood Type: " + bloodType);
    }
}
