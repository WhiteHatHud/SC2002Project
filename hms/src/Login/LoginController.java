package Login;

import ResetInfo.ResetPasswordController;
import Utilities.UserInputHandler;
import java.util.Map;

public class LoginController implements ControllerInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private LoginInt loginManager;
    private Map<String, UserRegistry> registries;
    private AccountsInit accountsInit;

    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries;
        this.loginManager = null;
        this.accountsInit = new AccountsInit("Patient List CSV.csv", registries);
    }

    @Override
    public boolean handleChoice(int choice) {
        if (choice == '~') {
            // Return to the welcome screen when '~' is pressed
            return true;  // Signal to show the welcome screen again
        }
    
        switch (choice) {
            case 1:
                boolean validChoice = false;
    
                while (!validChoice) {
                    displayManager.showLoginScreen();
                    String input = inputHandler.getNextLine().trim();
                    
                    if (input.equals("~")) {
                        System.out.println("Returning to the previous menu...");
                        return true;  // Signal to show the welcome screen again
                    }
            
                    try {
                        int option = Integer.parseInt(input);
                        DisplayFormat.clearScreen();
            
                        if (option == 1) {  // Patient
                            loginManager = new PatientLoginManager(displayManager, inputHandler, registries);
                            break;  // Exit the loop and proceed with login
                        } else if (option == 2) {  // Staff
                            loginManager = new StaffLoginManager(displayManager, inputHandler, registries);
                            break;  // Exit the loop and proceed with login
                        } else {
                            System.out.println("Invalid choice! Please reselect.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                    }
                }
            
                if (loginManager != null) {
                    boolean continueToMainMenu = !loginManager.start();
            
                    if (continueToMainMenu) {
                        return true;  // Signal to show the welcome screen again
                    }
                }
            
                return true; 
    
            case 2:
                System.out.println("Resetting password functionality...");
                ResetPasswordController resetPasswordController = new ResetPasswordController();
                boolean shouldReload = resetPasswordController.start();
    
                if (shouldReload) {
                    System.out.println("Reloading patient data...");
                    accountsInit.start();  // Reload patient data after password reset
                }
                return true;  // Return to the main menu after resetting password
            case 3:
                System.out.println("System shutting down...");
                System.exit(0);
                return false;  // Ends the program, won't return to menu
            default:
                System.out.println("Invalid choice");
                return true;  // Re-show the menu for invalid input
        }
    }
    
}
