package SC2002Project.hms.login;

import SC2002Project.hms.ui.DisplayManager;

public class LoginPageMain {
    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();  // Create one instance
        UserInputHandler inputHandler = new UserInputHandler();
        LoginManager loginManager = new LoginManager(displayManager,inputHandler);
        


        displayManager.showWelcomeScreen();
        int choice = inputHandler.getUserChoice();  // Get user choice for login, reset, quit, etc.
        loginManager.handleChoice(choice);  // Pass user choice to handleChoice
    }
}
