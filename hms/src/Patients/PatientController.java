package Patients;

import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Utilities.LogoutTimer;
import appt.PatientUI;

/**
 * Controls the flow and operations for a patient in the system,
 * including viewing medical records, updating personal information, 
 * managing appointments, and logging out.
 */
public class PatientController implements ControllerInt {
    private Patient patient;

    /**
     * Initializes the PatientController with the specified patient.
     *
     * @param patient the patient associated with this controller
     */
    public PatientController(Patient patient) {
        this.patient = patient;
    }

    /**
     * Starts the patient interface, allowing the patient to interact with the system.
     * @return false if the patient logs out, true otherwise
     */
    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + patient.getName()); 
        
        boolean isActive = true;
        while (isActive) { 
            PatientShared.getDisplayManager().displayMenu(); 
            int choice = PatientShared.getUserInputHandler().getUserChoice(); 
            isActive = handleChoice(choice); 
        }
        
        return false; 
    }

    /**
     * Handles the patient's menu choices.
     * @param choice the option chosen by the patient
     * @return true if the session should continue, false if the session should end
     */
    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1: // View records
                ViewMedicalRecord view = new ViewMedicalRecord();
                view.viewMedicalRecord(patient);
                break;

            case 2: // Update personal information
                UpdateInfo updateInfo = new UpdateInfo(patient);
                updateInfo.updatePersonalInformation();
                break;

            case 3: // Manage appointment matters
                try {
                    PatientUI ui = new PatientUI(patient.getUserID(), patient.getName());
                    ui.start();
                } catch (Exception e) {
                    System.out.println("Error managing appointments: " + e.getMessage());
                }
                DisplayManager.clearScreen();
                break;

            case 4: // Logout
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
}
