package Login;

import Patients.Patient;
import java.util.Map;

public class AuthenticationService {
    private Map<String, UserRegistry> registries;

    public AuthenticationService(Map<String, UserRegistry> registries) {
        this.registries = registries;
    }


    public boolean authenticate(String userType, String userID, String password) {

        UserRegistry registry = registries.get(userType);

        // Ensure that the registry exists for the given user type
        if (registry == null) {
            System.out.println("No registry found for user type: " + userType);
            return false;
        }

        Patient patient = (Patient) registry.findUserByID(userID);
        if (patient != null && patient.getPassword().equals(password)) {
            System.out.println("Authentication successful for userID: " + userID);
            return true;
        } else {
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println("              Authentication failed: Invalid userID or password.");
            return false;
        }
    }
}
