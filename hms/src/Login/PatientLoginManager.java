package Login;
import Patients.Patient;
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
    public boolean start() {
        returnToMenu = false;  
        Patient authenticatedPatient = null;
    
        while (authenticatedPatient == null) {
            displayManager.showPatientLoginID();
            String userID = inputHandler.getInput();
    
            // Check if the user wants to go back
            if (userID.equals("~")) {
                System.out.println("Returning to the previous menu...");
                returnToMenu = true; 
                return false;
            }
    
            displayManager.showEnterPW();
            Masking mask = new Masking();
            String password = mask.readPasswordWithMasking();
    
            authenticatedPatient = authenticationService.authenticate("patient", userID, password);
    
            if (authenticatedPatient != null) {
                DisplayFormat.clearScreen();
                PatientController pc = new PatientController(authenticatedPatient); // Pass authenticated patient to PatientController
                pc.start();
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }
        return false;
    }
    

    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
