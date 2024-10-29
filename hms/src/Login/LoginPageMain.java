package Login;
import Patients.PatientRegistry;
import Utilities.UserInputHandler;
import java.util.HashMap;
import java.util.Map;

public class LoginPageMain {
    public static void main(String[] args) {

        PatientRegistry patientRegistry = new PatientRegistry();
        Map<String, UserRegistry> registries = new HashMap<>();
        registries.put("patient", patientRegistry);

        DisplayManager displayManager = new DisplayManager();
        UserInputHandler inputHandler = new UserInputHandler();
        displayManager.clearScreen();
        // Initialize AccountsInit to load patient data into patientRegistry
        AccountsInit patientInit = new AccountsInit("././Patient List CSV.csv", registries);
        patientInit.start();

        LoginManager loginManager = new LoginManager(displayManager, inputHandler, registries);
        loginManager.startLoginProcess();
    }
}


