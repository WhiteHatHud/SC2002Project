package Login;

public class LoginPageMain {
    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        UserInputHandler inputHandler = new UserInputHandler();
        LoginManager loginManager = new LoginManager(displayManager, inputHandler);
    
        loginManager.startLoginProcess();
    }
    
}
