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
        displayManager.showWelcomeScreen();
        int choice = inputHandler.getUserChoice(); 
        LoginController.handleChoice(choice); 
    }
    

}
