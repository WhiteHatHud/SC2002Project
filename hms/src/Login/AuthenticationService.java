package Login;

import Patients.Patient;
import java.util.Map;

public class AuthenticationService {
    private Map<String, UserRegistry> registries;

    public AuthenticationService(Map<String, UserRegistry> registries) {
        this.registries = registries;
    }

    public Patient authenticate(String userType, String userID, String password) {
        UserRegistry registry = registries.get(userType);

        // Ensure that the registry exists for the given user type
        if (registry == null) {
            System.out.println("No registry found for user type: " + userType);
            return null; 
        }

        Patient patient = (Patient) registry.findUserByID(userID);
        
        // Check if the patient exists and the password matches
        if (patient != null && patient.getPassword().equals(password)) {
            System.out.println("Authentication successful for userID: " + userID);
            return patient; 

        } else {
            DisplayFormat.clearScreen();
            System.out.println("              Authentication failed: Invalid userID or password.");
            return null; 
        }
    }
}
