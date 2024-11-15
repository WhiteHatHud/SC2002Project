package Login;

import Patients.PatientRegistry;
import Utilities.UserInputHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for the Hospital Management System's login process.
 * This class initializes required components, loads patient data,
 * and starts the login process.
 */
public class LoginPageMain {

    /**
     * The main method initializes and sets up necessary components for the login system.
     * It loads patient data, prepares display and input handlers, and begins the login process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        // Initialize the patient registry and set up user registries map
        PatientRegistry patientRegistry = new PatientRegistry();
        Map<String, UserRegistry> registries = new HashMap<>();
        registries.put("patient", patientRegistry);

        // Initialize display and input handlers
        DisplayManager displayManager = new DisplayManager();
        UserInputHandler inputHandler = new UserInputHandler();
        DisplayManager.clearScreen();
        
        // Initialize AccountsInit to load patient data into patientRegistry
        AccountsInit patientInit = new AccountsInit("Patient List CSV.csv", registries);
        patientInit.start();

        // Initialize and start the login process
        LoginManager loginManager = new LoginManager(displayManager, inputHandler, registries);
        loginManager.startLoginProcess();
    }
}
