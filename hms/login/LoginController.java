package SC2002Project.hms.login;

import SC2002Project.hms.ui.DisplayManager;

public class LoginController {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private PatientLoginManager patientLoginManager;  
    private StaffLoginManager staffLoginManager;      

    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.patientLoginManager = new PatientLoginManager();
        this.staffLoginManager = new StaffLoginManager();
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                // If login is selected, determine whether they are a patient or staff
                int option = inputHandler.getUserChoice();
                if (option == 1) { // Patient
                    patientLoginManager.handlePatientLogin();  // Delegate to PatientLoginManager
                } else if (option == 2) { // Staff
                    staffLoginManager.handleStaffLogin();      // Delegate to StaffLoginManager
                } else {
                    System.out.println("Invalid choice! Please reselect.");
                }
                break;
            case 2:
                // reset password 
                break;
            case 3:
                System.out.println("System shutting down...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}
