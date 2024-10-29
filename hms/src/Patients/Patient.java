package Patients;
import Users.Users;

public class Patient extends Users {

    private String dob;
    private String gender;
    private String bloodType;
    private String email;


    public Patient(String patientID, String name, String dob, String gender, String bloodType, String contactInfo) {
        super(patientID, name); 
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


    public void viewProfile() {
        System.out.println("Patient ID: " + userID + ", Name: " + name + 
                           ", Email: " + email + ", DOB: " + dob + 
                           ", Gender: " + gender + ", Blood Type: " + bloodType);
    }
}
