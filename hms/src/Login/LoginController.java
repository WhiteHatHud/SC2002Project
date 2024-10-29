package Login;

import Utilities.UserInputHandler;
import java.util.Map;

public class LoginController implements ControllerInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private LoginInt loginManager;
    private Map<String, UserRegistry> registries;

    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries;
        this.loginManager = null;
    }

    @Override
    public boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                boolean validChoice = false;

                while (!validChoice) {
                    displayManager.showLoginScreen();
                    int option = inputHandler.getUserChoice();
                    displayManager.clearScreen();
                    
                    if (option == 1) {  // Patient
                        loginManager = new PatientLoginManager(displayManager, inputHandler, registries);
                        validChoice = true;
                    } else if (option == 2) {  // Staff
                        loginManager = new StaffLoginManager();
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice! Please reselect.");
                    }

                    if (loginManager != null) {
                        boolean continueToMainMenu = !loginManager.start(); 
                        
                        if (continueToMainMenu) {
                            validChoice = false; // Reset validChoice to re-display the main menu
                        }
                    }
                }
                break;

            case 2:
                System.out.println("Resetting password functionality...");
                return true; // Continue to show main menu

            case 3:
                System.out.println("System shutting down...");
                System.exit(0);
                break;

            default:
                System.out.println("Invalid choice");
        }
        return true;
    }
}
