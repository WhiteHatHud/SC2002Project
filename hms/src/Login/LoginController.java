package Login;
import java.util.Map;

public class LoginController {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private LoginInt loginManager;  
    private Map<String, UserRegistry> registries; 

    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries; // Initialize the registries map
        this.loginManager = null;  
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                boolean validChoice = false;

                while (!validChoice) {
                    // Display the "I am a..." menu
                    displayManager.showLoginScreen();
                    int option = inputHandler.getUserChoice();

                    if (option == 1) {  // Patient
                        loginManager = new PatientLoginManager(displayManager, inputHandler, registries);
                        validChoice = true;
                    } else if (option == 2) {  // Staff
                        loginManager = new StaffLoginManager();
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice! Please reselect.");
                    }
                }

                if (loginManager != null) {
                    loginManager.start();

                    // Check if user wants to return to the main menu after `PatientLoginManager.start()`
                    if (loginManager instanceof PatientLoginManager && ((PatientLoginManager) loginManager).shouldReturnToMenu()) {
                        validChoice = false; // Reset validChoice to re-display the "I am a..." menu
                    }
                }
                break;

            case 2:
                System.out.println("Resetting password functionality...");
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
