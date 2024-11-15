package Login;

import ResetInfo.ResetPasswordController;
import Utilities.UserInputHandler;
import java.util.Map;

/**
 * Manages the login flow and menu choices for the Hospital Management System.
 * This class interacts with the display manager, input handler, and login managers
 * to guide users through login and password reset functionalities.
 */
public class LoginController implements ControllerInt {
    
    /** Manages the display of various screens in the login process. */
    private DisplayManager displayManager;
    
    /** Handles user input for menu selections and data entry. */
    private UserInputHandler inputHandler;
    
    /** Manages specific login functionality based on user type (Patient or Staff). */
    private LoginInt loginManager;
    
    /** Stores user registries by type, used to initialize accounts. */
    private Map<String, UserRegistry> registries;
    
    /** Initializes accounts from a CSV file. */
    private AccountsInit accountsInit;

    /**
     * Constructs a LoginController with specified display manager, input handler,
     * and user registries. It also initializes the account information from a CSV file.
     *
     * @param displayManager Manages screen displays for login prompts and messages.
     * @param inputHandler   Handles user input.
     * @param registries     A map of user type to user registry.
     */
    public LoginController(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries;
        this.loginManager = null;
        this.accountsInit = new AccountsInit("Patient List CSV.csv", registries);
    }

    /**
     * Handles a user's choice from the main menu and performs the associated action.
     * Choices include login as Patient or Staff, reset password, or quit the system.
     *
     * @param choice The user's menu choice.
     * @return {@code true} to reload the welcome screen; {@code false} to exit the program.
     */
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
