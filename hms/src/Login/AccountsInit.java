package Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class AccountsInit {

    private String csvFilePath;  
    private Map<String, UserRegistry> registries;  // Map to hold registries for various user types

    // Constructor accepting a map of UserRegistries
    public AccountsInit(String csvFilePath, Map<String, UserRegistry> registries) {
        this.csvFilePath = csvFilePath;
        this.registries = registries;  // A map of user type -> UserRegistry
    }

    public void start() {
        readAndProcessCSV();
    }

    public void readAndProcessCSV() {
        String line;
        String csvDelimiter = ",";
        String userType = ""; 
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine(); // Skip header
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvDelimiter);
                userType = determineUserType(csvFilePath); 
    
                // Create user obj, then get registry from dict
                Users user = UserFactory.createUser(userType, values);
                UserRegistry registry = registries.get(userType);
                
                if (registry != null) {
                    registry.addUser(user);
                } else {
                    System.out.println("No registry found for user type: " + userType);
                }
            }
    
            System.out.println("Accounts successfully initialized from " + csvFilePath + ". The " + userType + " accounts.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private String determineUserType(String csvFilePath) {
        if (csvFilePath.contains("Patient")) {
            return "patient";
        } else if (csvFilePath.contains("Doctor")) {
            return "doctor";
        }
        return "unknown";
    }
}
