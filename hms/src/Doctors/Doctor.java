package Doctors;
import Users.Staff;

public class Doctor extends Staff {
    public Doctor() {
        super();
    }

    public Doctor(String staffID, String name, String role, String gender, int age, String officeNumber) {
        super(staffID, name, role, gender, age, officeNumber);
    }

    public void getProfile() {
        System.out.println("=== Doctor Profile ===");
        System.out.printf("%-20s: %s%n", "Doctor ID", userID);
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Role", role);
        System.out.printf("%-20s: %s%n", "Gender", gender);
        System.out.printf("%-20s: %d%n", "Age", age);
        System.out.printf("%-20s: %s%n", "Office Number", officeNumber);
        System.out.println("========================");
    }

    @Override
    public String toCSV() {
        return String.join(",", userID, name, role, gender, String.valueOf(age), officeNumber);
    }
}
