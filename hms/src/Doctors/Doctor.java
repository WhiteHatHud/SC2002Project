package Doctors;
import Users.Staff;

public class Doctor extends Staff {
    private String specialization;
    private String contactNumber;
    private String email;
    private String password;

    // Constructor
    public Doctor(String doctorID, String name, String role, String gender, int age, String officeNumber, String specialization, String contactNumber, String email, String password) {
        super(doctorID, name, role, gender, age, officeNumber);
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter methods specific to Doctor
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void getProfile() {
        System.out.println("=== Doctor Profile ===");
        System.out.printf("%-20s: %s%n", "Doctor ID", userID);
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Role", role);
        System.out.printf("%-20s: %s%n", "Gender", gender);
        System.out.printf("%-20s: %d%n", "Age", age);
        System.out.printf("%-20s: %s%n", "Office Number", officeNumber);
        System.out.printf("%-20s: %s%n", "Specialization", specialization);
        System.out.printf("%-20s: %s%n", "Contact Number", contactNumber);
        System.out.println("========================");
    }

    @Override
    public String toCSV() {
        return String.join(",", userID, name, role, gender, String.valueOf(age), officeNumber, specialization, contactNumber, email);
    }
}
