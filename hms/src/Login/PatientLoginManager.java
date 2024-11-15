package Login;

import Patients.Patient;
import Patients.PatientController;
import Utilities.Masking;
import Utilities.UserInputHandler;
import java.util.Map;

/**
 * Manages the login process for patients. This class prompts patients for their user ID
 * and password, authenticates them using {@link AuthenticationService}, and, if successful,
 * launches the {@link PatientController}.
 */
public class PatientLoginManager implements LoginInt {

    /** Manages display for login prompts and messages. */
    private DisplayManager displayManager;

    /** Handles user input during the login process. */
    private UserInputHandler inputHandler;

    /** Authenticates patients by verifying their credentials. */
    private AuthenticationService authenticationService;

    /** Flag indicating if the user wants to return to the main menu. */
    private boolean returnToMenu = false;

    /**
     * Constructs a PatientLoginManager with the specified display manager, input handler,
     * and user registries. Initializes the authentication service for patient login.
     *
     * @param displayManager Manages display screens for login prompts and messages.
     * @param inputHandler   Handles user input during the login process.
     * @param registries     A map of user type to user registry (not directly used in this class).
     */
    public PatientLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.authenticationService = new AuthenticationService();
    }

    /**
     * Starts the login process for a patient. This method prompts for the patient’s user ID
     * and password, authenticates the credentials, and initiates the {@link PatientController}
     * for authenticated patients. Allows the patient to return to the main menu by entering '~'.
     *
     * @return {@code false} if login was successful or if the user chooses to return to the main menu.
     */
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
    
            authenticatedPatient = authenticationService.authenticate(userID, password);
    
            if (authenticatedPatient != null) {
                DisplayFormat.clearScreen();
                PatientController pc = new PatientController(authenticatedPatient); // Pass authenticated patient to PatientController
                pc.start();
            } else {
                System.out.println("-");
            }
        }
        return false;
    }

    /**
     * Checks if the user has chosen to return to the main menu.
     *
     * @return {@code true} if the user wants to return to the main menu, {@code false} otherwise.
     */
    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
