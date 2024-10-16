package SC2002Project.hms.login;
import SC2002Project.hms.ui.DisplayManager;

class LoginManager {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;

    public LoginManager(DisplayManager displayManager, UserInputHandler inputHandler) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
    }

    public void startLoginProcess() {
        displayManager.showWelcomeScreen();  // Show welcome screen
        int userChoice = inputHandler.getUserChoice();  // Capture user choice
        handleChoice(userChoice);  // Delegate the handling to the controller
    }
    public void handleChoice(int userChoice) {
        LoginController loginController = new LoginController(displayManager, inputHandler);
        loginController.handleChoice(userChoice);  // Pass choice to LoginController
    }

    public int userChoice() {
        int choice = inputHandler.getUserChoice();
        return choice;
    }
    
    

}
