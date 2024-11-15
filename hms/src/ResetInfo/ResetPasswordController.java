package ResetInfo;

import java.util.HashMap;
import java.util.Map;

import Login.AccountsInit;
import Users.Staff;
import Patients.Patient;
import Patients.PatientData;
import Login.DisplayManager;
import Login.ControllerInt;
import Utilities.CSVUpdater;
import Utilities.UserInputHandler;
import Utilities.CSVUtilities;
import Users.StaffData;


public class ResetPasswordController implements ControllerInt {
    private DisplayManager display;
    private UserInputHandler input;
    private CSVUpdater csvUpdaterPatient;
    private CSVUpdater csvUpdaterStaff;
    private CSVUtilities utilities;
    private StaffData staffData;
    private PatientData patientData;
    private String errorMessage;
    private AccountsInit accounts;
    

    public ResetPasswordController() {
        this.display = new DisplayManager();
        this.input = new UserInputHandler();
        this.csvUpdaterPatient = new CSVUpdater("Patient List CSV.csv");
        this.csvUpdaterStaff = new CSVUpdater("Staff_List.csv");
        this.staffData = new StaffData();  // Access to staff information
        this.patientData = new PatientData();
        this.errorMessage = "";

    }

    public boolean start() {
        boolean isRunning = true;
        while (isRunning) {
            DisplayManager.clearScreen();
            displayResetMenu();
            int choice = input.getUserChoice();
            if (choice == 3) {
                // Return to main menu option
                return true;  // Signal to return to main menu
            }
            isRunning = handleChoice(choice);
        }
        return false;
    }

    // Display reset menu options for user to choose between Staff or Patient
    private void displayResetMenu() {
        //DisplayManager.clearScreen();
        if (errorMessage != ""){
            DisplayManager.printCentered(errorMessage, 80);
            display.divider();
            errorMessage = "";
        }
        System.out.println("Reset Password Menu");
        System.out.println("1. Reset Patient Password");
        System.out.println("2. Reset Staff Password");
        System.out.println("3. Return to Previous Menu");
        display.divider();
        System.out.print("Enter your choice: ");
        
    }

    @Override
    public boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                resetPatientPassword();
                break;
            case 2:
                resetStaffPassword();
                break;
            case 3:
                System.out.println("Returning to previous menu...");
                break;  // Exit the loop to return
            default:
                DisplayManager.invalidChoice();
                break;
        }
        return true;
    }

    // Method to reset a staff member's password with verification using name, role, and office number
    private void resetStaffPassword() {
        DisplayManager.clearScreen();
        display.showStaffLoginID();
        String staffID = input.getNextLine();

        // Retrieve staff by ID using StaffData
        Staff staff = staffData.getStaffByID(staffID);
        if (staff == null) {
            errorMessage = "Invalid Staff ID";
            return;
        }

        display.divider();
        // Verify with staff's name, role, and office number
        System.out.print("Enter your name: ");
        String name = input.getNextLine();
        System.out.print("Enter your role: ");
        String role = input.getNextLine();
        System.out.print("Enter your office number: ");
        String officeNumber = input.getNextLine();

        // Validation check
        if (validateStaffIdentity(staff, name, role, officeNumber)) {
            display.divider();
            DisplayManager.passwordUpdate();
            String newPassword = input.getNextLine();
            csvUpdaterStaff.updateField(staffID, "Password", newPassword);
            errorMessage = "Password reset successfully";
        } else {
            failReturn("Invalid verification details. Password reset failed.");
        }
    }

    // Method to reset a patient's password with date of birth verification
    private void resetPatientPassword() {
        DisplayManager.clearScreen();
        display.showPatientLoginID();
        String patientID = input.getNextLine();

        // Assuming we have a method to retrieve patients by ID, similar to StaffData
        Patient patient = patientData.getPatientByID(patientID);
        if (patient == null) {
            errorMessage = "Invalid Patient ID";
            return;
        }

        // Verify with patient's date of birth
        display.divider();
        System.out.print("Enter your date of birth (YYYY-MM-DD): ");
        String dob = input.getNextLine();

        // Validation check
        if (validatePatientIdentity(patient, dob)) {
            display.divider();
            DisplayManager.passwordUpdate();
            String newPassword = input.getNextLine();
            csvUpdaterPatient.updateField(patientID, "Password", newPassword);
            errorMessage = "Password reset successfully"; 

        } else {
            failReturn("Invalid date of birth. Password reset failed.");
        }
    }

    // Method to validate staff identity using name, role, and office number
    private boolean validateStaffIdentity(Staff staff, String name, String role, String officeNumber) {
        return staff.getName().equalsIgnoreCase(name) &&
               staff.getRole().equalsIgnoreCase(role) &&
               staff.getOfficeNumber().equalsIgnoreCase(officeNumber);
    }

    // Method to validate patient identity using date of birth
    private boolean validatePatientIdentity(Patient patient, String dob) {
        return patient.getDateOfBirth().equals(dob);  // Assuming date of birth is stored as a string
    }

    private void failReturn(String message){
        display.divider();
        DisplayManager.printCentered(message,80);
        DisplayManager.printCentered("Press enter to return to main menu", 80);
        input.getNextLine();
    }
}
