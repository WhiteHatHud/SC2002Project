package Doctors;

import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Utilities.LogoutTimer;
import appt.DoctorUI;
import java.util.List;

public class DoctorController implements ControllerInt {

    private Doctor doctor;

    public DoctorController(Doctor doctor) {
        this.doctor = doctor;
    }

    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + doctor.getName());

        boolean isActive = true;
        while (isActive) {
            DoctorShared.getDisplayManager().getDisplayMenu();
            int choice = DoctorShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice); // Keeps looping until the user chooses to logout
        }

        return false; // Ends session if logged out
    }

    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1:
                // View patient under my care
                viewPatientUnderMyCare();
                break;
    
            case 2: // Create patient medical record
                CreateRecord record = new CreateRecord(doctor.getUserID());
                record.createRecord();
                break;
    
            case 3: // Update record
                UpdateRecord update = new UpdateRecord(doctor.getUserID());
                update.updateRecord();
                break;
    
            case 4: 
                DoctorUI ui = new DoctorUI(doctor.getUserID(), doctor.getName());
                ui.start();
                DisplayManager.clearScreen();
                break;
    
            case 5: // Logout
                if (LogoutTimer.confirmLogout()) {
                    return false; // Ends the session only if logout is confirmed
                } else {
                    DisplayManager.clearScreen();
                    
                    return true; // Continue the session without printing additional messages
                }
    
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    
        return true; // Continue session if choice is not logout
    }
    

    public void viewPatientUnderMyCare() {
        // Retrieve the list of patient IDs under the doctor’s care
        List<String> patientIDs = DoctorShared.getcsvUtilities().getPatientIDsUnderDoctorCare(doctor.getUserID());
    
        // Check if there are any patients under the doctor’s care
        if (patientIDs.isEmpty()) {
            System.out.println("No patients found under your care.");
            return;
        }
    
        // Print the number of patients under the doctor's care
        System.out.println("There are " + patientIDs.size() + " patient(s) under your care.");
        System.out.println("=== Patient(s) Under Your Care ===");
    
        int count = 1; 

        for (String patientID : patientIDs) {
            System.out.println("\n--- Patient Information " + count + " ---"); 
            DoctorShared.getcsvUtilities().getPatientInformation(patientID);
            count++; 
        }
        
    }
    
    
}
