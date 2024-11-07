package Patients;
import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Utilities.LogoutTimer;
import appt.PatientUI;

public class PatientController implements ControllerInt {
    private Patient patient;
    private PatientShared instance = new PatientShared();
    
    public PatientController(Patient patient) {
        this.patient = patient;
    }

    public boolean start() {
        System.out.println("Welcome, " + patient.getName()); 
        
        boolean isActive = true;
        while (isActive) { 
            PatientShared.getDisplayManager().displayMenu(); 
            int choice = PatientShared.getUserInputHandler().getUserChoice(); 
            isActive = handleChoice(choice); 

        }
        
        return false; 
    }
    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            
            case 1: // View records
                ViewMedicalRecord view = new ViewMedicalRecord();
                view.viewMedicalRecord(patient);
                break;
            
            case 2: // Update personnal info 
                UpdateInfo updateInfo = new UpdateInfo(patient);
                updateInfo.updatePersonalInformation();
                break;

            case 3: //Manage appointment matters
                PatientUI ui = new PatientUI(patient.getUserID(), patient.getName());
                ui.start();
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