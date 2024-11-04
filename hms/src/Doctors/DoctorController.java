package Doctors;

import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Utilities.LogoutTimer;
import appt.DoctorUI;

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
            case 1: // View patient record
                
                break;

            case 2: // Create patient medical record
                CreateRecord record = new CreateRecord(doctor.getUserID());
                record.createRecord();
                break;

            case 3: // Update record
                
                break;

            case 4: 
                DoctorUI ui = new DoctorUI(doctor.getUserID(), doctor.getName());
                ui.start(); 
                DisplayManager.clearScreen();
                break;

            case 5: // Logout
                LogoutTimer.confirmLogout(); // Confirms logout
                return false; // Ends the session by returning false

            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        return true; // Continue session if choice is not logout
    }
}
