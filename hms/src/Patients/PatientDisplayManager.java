package Patients;

import Login.DisplayManager;

/**
 * Handles the display menus for patient-related actions and information updates.
 * Extends DisplayManager to provide additional functionalities specific to patient interactions.
 */
public class PatientDisplayManager extends DisplayManager {
    
    /**
     * Displays the main menu options for a patient.
     */
    public void displayMenu() {
        System.out.println("=== Patient Menu ===");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. Manage Appointment Matters");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
    }

    /**
     * Displays the update options menu for a patient’s personal information.
     */
    public void getUpdateMenu() {
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Email");
        System.out.println("2. Contact Number");
        System.out.println("3. Emergency Contact");
        System.out.println("4. Change Password");
        System.out.println("5. Done (Exit Update)");
        System.out.print("Enter your choice: ");
    }

    /**
     * Displays the appointment management menu, allowing patients to schedule, view, or cancel appointments.
     */
    public void apptMenu() {
        System.out.println("\n=== Appointment Management Menu ===");
        System.out.println("1. Schedule an Appointment");
        System.out.println("2. Cancel an Appointment");
        System.out.println("3. View Scheduled Appointments");
        System.out.println("4. View Past Appointment Outcome Records");
        System.out.println("5. Back To Main Menu");
        System.out.print("Enter your choice: ");
    }
}
