package Patients;
import Users.Users;

public class Patient extends Users {

    private String dob;
    private String gender;
    private String bloodType;
    private String email;
    private String number;
    private String enumber;

    public Patient(String patientID, String name, String dob, String gender, String bloodType, String contactInfo,String number, String enumber) {
        super(patientID, name); 
        this.dob = dob;             
        this.gender = gender;
        this.bloodType = bloodType;
        this.email = contactInfo;
        this.number = number;
        this.enumber = enumber;
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

    public String getNumber() {
        return number;
    }

    public String getenumber() {
        return enumber;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setenumber(String enumber) {
        this.enumber = enumber;
    }

    public void getProfile() {
        System.out.println("=== Patient Profile ===");
        System.out.printf("%-20s: %s%n", "Patient ID", userID);
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Email", email);
        System.out.printf("%-20s: %s%n", "Date of Birth", dob);
        System.out.printf("%-20s: %s%n", "Gender", gender);
        System.out.printf("%-20s: %s%n", "Blood Type", bloodType);
        System.out.printf("%-20s: %s%n", "Contact Number", number);
        System.out.printf("%-20s: %s%n", "Emergency Contact", enumber);
        System.out.println("========================");
    }
    
    
}
