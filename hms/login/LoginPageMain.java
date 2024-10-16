package SC2002Project.hms.login;

import SC2002Project.hms.ui.DisplayManager;

public class LoginPageMain {
    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        UserInputHandler inputHandler = new UserInputHandler();
        LoginManager loginManager = new LoginManager(displayManager, inputHandler);
    
        loginManager.startLoginProcess();
    }
    
}
