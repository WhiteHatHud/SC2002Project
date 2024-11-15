package Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import Users.*;

/**
 * Initializes user accounts by reading data from a CSV file and populating user registries.
 * This class processes CSV files to determine the user type (e.g., "patient" or "doctor"),
 * creates user objects, and adds them to the corresponding user registry.
 */
public class AccountsInit {

    /**
     * Path to the CSV file that contains user data.
     */
    private String csvFilePath;  

    /**
     * Map of user type to user registry, where each entry corresponds to a type of user
     * (e.g., "patient" or "doctor") and its associated registry for storing user accounts.
     */
    private Map<String, UserRegistry> registries;  

    /**
     * Constructs an AccountsInit object with a specified CSV file path and a map of user registries.
     *
     * @param csvFilePath The path to the CSV file containing user data.
     * @param registries  A map of user type to user registry, used to store different types of users.
     */
    public AccountsInit(String csvFilePath, Map<String, UserRegistry> registries) {
        this.csvFilePath = csvFilePath;
        this.registries = registries;
    }

    /**
     * Starts the account initialization process by reading and processing the CSV file.
     */
    public void start() {
        readAndProcessCSV();
    }

    /**
     * Reads the CSV file specified by csvFilePath and processes each row to create and add users
     * to the appropriate registry based on their user type.
     * 
     * This method uses {@link #determineUserType(String)} to identify the user type from the CSV file path
     * and {@link UserFactory#createUser(String, String[])} to create user objects. If a matching registry
     * is found in the registries map, the user is added; otherwise, an error message is printed.
     * 
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public void readAndProcessCSV() {
        String line;
        String csvDelimiter = ",";
        String userType = ""; 
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine(); // Skip header
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvDelimiter);
                userType = determineUserType(csvFilePath); 
    
                // Create user object and get registry from map
                Users user = UserFactory.createUser(userType, values);
                UserRegistry registry = registries.get(userType);
                
                if (registry != null) {
                    registry.addUser(user);
                } else {
                    System.out.println("No registry found for user type: " + userType);
                }
            }
    
            //System.out.println("Accounts successfully initialized from " + csvFilePath + ". The " + userType + " accounts.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the user type based on the CSV file path.
     * This method checks if the file path contains specific keywords to identify the user type.
     * 
     * @param csvFilePath The path of the CSV file.
     * @return A string representing the user type ("patient", "doctor", or "unknown").
     */
    private String determineUserType(String csvFilePath) {
        if (csvFilePath.contains("Patient")) {
            return "patient";
        } else if (csvFilePath.contains("Doctor")) {
            return "doctor";
        }
        return "unknown";
    }
}
