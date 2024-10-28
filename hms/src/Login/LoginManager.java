package Login;
import Utilities.UserInputHandler;
import java.util.Map;

public class LoginManager {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private Map<String, UserRegistry> registries; // Store the registries map

    public LoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.registries = registries; // Initialize the registries map
    }

    public void startLoginProcess() {
        LoginController loginController = new LoginController(displayManager, inputHandler, registries);
        displayManager.showWelcomeScreen();
        int userChoice = inputHandler.getUserChoice();
        displayManager.clearScreen();
        loginController.handleChoice(userChoice); 
        
    }
}
