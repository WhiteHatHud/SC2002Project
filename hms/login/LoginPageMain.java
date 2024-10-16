package SC2002Project.hms.login;
import java.util.*;
import SC2002Project.hms.ui.*;


class LoginPageMain {
    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        LoginManager loginManager = new LoginManager();
        UserInputHandler inputHandler = new UserInputHandler();
        
        displayManager.showWelcomeScreen();
        int choice = inputHandler.getUserChoice();
        
        loginManager.handleChoice(choice);
    }
}