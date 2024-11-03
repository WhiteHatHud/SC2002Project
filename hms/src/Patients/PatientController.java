package Patients;
import Login.ControllerInt;
import Login.DisplayFormat;
import Utilities.LogoutTimer;

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
                

            case 9: 
                LogoutTimer.confirmLogout();
                return false; 
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
            
                
        }
        
        return true; // Continue session if choice is not logout
    }
}
