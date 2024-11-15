package Doctors;

import Login.DisplayManager;
import Users.Staff;

/**
 * The {@code Doctor} class represents a doctor, extending the {@code Staff} class with additional
 * profile display and CSV export capabilities.
 */
public class Doctor extends Staff {

    /**
     * Default constructor for {@code Doctor}, initializing a new doctor with default values.
     */
    public Doctor() {
        super();
    }

    /**
     * Constructs a {@code Doctor} object with specified details.
     *
     * @param staffID     The unique ID of the doctor.
     * @param name        The name of the doctor.
     * @param role        The role of the doctor.
     * @param gender      The gender of the doctor.
     * @param age         The age of the doctor.
     * @param officeNumber The office number of the doctor.
     * @param password    The password for the doctor's account.
     */
    public Doctor(String staffID, String name, String role, String gender, int age, String officeNumber, String password) {
        super(staffID, name, role, gender, age, officeNumber, password);
    }

    /**
     * Displays the doctor's profile, including their ID, name, role, gender, age, and office number.
     */
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

    /**
     * Converts the doctor's details into a CSV-formatted string.
     *
     * @return A CSV string representation of the doctor's information.
     */
    @Override
    public String toCSV() {
        return String.join(",", userID, name, role, gender, String.valueOf(age), officeNumber, password);
    }
}
