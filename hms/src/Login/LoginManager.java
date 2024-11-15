package Login;

import Utilities.UserInputHandler;
import java.util.Map;

/**
 * Manages the login process for the Hospital Management System.
 * This class initializes and controls the flow of user login and interacts with
 * {@link LoginController} to handle user choices and display screens.
 */
public class LoginManager {
    
    /** Manages the display of various login-related screens. */
    private DisplayManager displayManager;
    
    /** Handles user input for menu selections and other interactions. */
    private UserInputHandler inputHandler;
    
    /** Stores user registries by type, used to initialize user accounts for login. */
    private Map<String, UserRegistry> registries; 

    /**
     * Constructs a LoginManager with the specified display manager, input handler, and user registries.
     *
     * @param displayManager Manages screen displays for login prompts and messages.
     * @param inputHandler   Handles user input for choices and data entry.
     * @param registries     A map of user type to user registry, used to manage different types of users.
     */
    public LoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries;
    }

    /**
     * Starts the login process by displaying the welcome screen and handling user choices
     * through the {@link LoginController}. Continues to show the main menu until
     * the user decides to quit.
     */
    public void startLoginProcess() {
        LoginController loginController = new LoginController(displayManager, inputHandler, registries);
        boolean showMainMenu = true;
    
        while (showMainMenu) {
            DisplayManager.clearScreen();
            displayManager.showWelcomeScreen();
            int userChoice = inputHandler.getUserChoice();
            DisplayManager.clearScreen();
            showMainMenu = loginController.handleChoice(userChoice);
        }
    }
}
