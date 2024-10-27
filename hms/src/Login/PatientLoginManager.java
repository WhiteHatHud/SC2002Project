package Login;
import java.util.Map;

public class PatientLoginManager implements LoginInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private AuthenticationService authenticationService;
    private boolean returnToMenu = false; // Flag to indicate if user wants to return to menu

    public PatientLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.authenticationService = new AuthenticationService(registries);
    }

    @Override
    public void start() {
        boolean isAuthenticated = false;

        while (!isAuthenticated) {
            displayManager.showPatientLoginID();
            String userID = inputHandler.getInput();

            // Check if the user wants to go back
            if (userID.equals("~")) {
                System.out.println("Returning to the previous menu...");
                returnToMenu = true; // Set flag to true to indicate user wants to return
                return;
            }

            displayManager.showEnterPW();
            String password = inputHandler.getInput();

            isAuthenticated = authenticationService.authenticate("patient", userID, password);

            if (isAuthenticated) {
                System.out.println("Login successful! Welcome, Patient.");
            } else {
                System.out.println("Invalid credentials. Please try again.");
                System.out.println("Enter ~ to go back to the main menu.");
            }
        }
    }

    // Getter for returnToMenu flag
    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
