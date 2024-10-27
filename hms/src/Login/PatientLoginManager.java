package Login;
import Patients.PatientController;
import Utilities.Masking;
import Utilities.UserInputHandler;
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
        returnToMenu = false;  
        boolean isAuthenticated = false;
    
        while (!isAuthenticated) {
            displayManager.showPatientLoginID();
            String userID = inputHandler.getInput();
    
            // Check if the user wants to go back
            if (userID.equals("~")) {
                System.out.println("Returning to the previous menu...");
                returnToMenu = true; 
                return;
            }
    
            displayManager.showEnterPW();
            Masking mask = new Masking();

            String password = mask.readPasswordWithMasking();
    
            isAuthenticated = authenticationService.authenticate("patient", userID, password);
    
            if (isAuthenticated) {
                System.out.println("Login successful! Welcome, Patient.");
                PatientController pc = new PatientController();
                pc.start();
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
