package Login;

import Patients.PatientRegistry;
import java.util.HashMap;
import java.util.Map;

public class LoginPageMain {
    public static void main(String[] args) {
        PatientRegistry patientRegistry = new PatientRegistry();
        Map<String, UserRegistry> registries = new HashMap<>();
        registries.put("patient", patientRegistry);

        DisplayManager displayManager = new DisplayManager();
        UserInputHandler inputHandler = new UserInputHandler();
        LoginManager loginManager = new LoginManager(displayManager, inputHandler);
        PatientRegistry pt = new PatientRegistry();
        AccountsInit patientInit = new AccountsInit("/Users/tanjunhern/Documents/Patient List CSV.csv", registries);
        patientInit.start();
        patientRegistry.printAllPatients();
        loginManager.startLoginProcess();
    }
    
}
