package Patients;

import Login.ControllerInt;

public class PatientController implements ControllerInt {
    private Patient patient; 


    public PatientController(Patient patient) {
        this.patient = patient;
    }

    public void start() {
        System.out.println("Welcome, " + patient.getName()); 
        
        boolean isActive = true;
        while (isActive) { 
            PatientShared.getDisplayManager().displayMenu(); 
            int choice = PatientShared.getUserInputHandler().getUserChoice(); 
            isActive = handleChoice(choice); 
            return;
        }
    }


    public boolean handleChoice(int choice) {
        switch (choice) {
            case 1: //View records
                ViewMedicalRecord view = new ViewMedicalRecord();
                view.viewMedicalRecord(patient);
                break;
            case 9: // Logout
                //logout(); 
                return false; 
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return true; 
    }
}
