package Login;


class LoginManager {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;

    public LoginManager(DisplayManager displayManager, UserInputHandler inputHandler) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
    }

    public void startLoginProcess() {
        displayManager.showWelcomeScreen(); 
        int userChoice = inputHandler.getUserChoice();  
        handleChoice(userChoice);  // Delegate the handling to the controller
    }
    public void handleChoice(int userChoice) {
        LoginController loginController = new LoginController(displayManager, inputHandler);
        loginController.handleChoice(userChoice);  // Pass choice to LoginController
    }

    

}
