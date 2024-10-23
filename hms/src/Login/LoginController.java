package Login;

public class LoginController {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private LoginInt loginManager;  // Using interface type for polymorphism

    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.loginManager = null;  // Initialize loginManager as null
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                boolean validChoice = false;

                // Retry logic for invalid selections until a valid manager is assigned
                while (!validChoice) {
                    // Show login screen
                    displayManager.showLoginScreen();
                    int option = inputHandler.getUserChoice();

                    
                    if (option == 1) {  // Patient
                        loginManager = new PatientLoginManager(displayManager,inputHandler);  // Assign PatientLoginManager
                        validChoice = true;  
                    } else if (option == 2) {  // Staff
                        loginManager = new StaffLoginManager();  // Assign StaffLoginManager
                        validChoice = true; 
                    } else {
                        System.out.println("Invalid choice! Please reselect.");//later implement back
                    }
                }

                if (loginManager != null) {
                    loginManager.start();  // Polymorphic method call
                }
                break;

            case 2:
                // Reset password 
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
