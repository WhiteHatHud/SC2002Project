package Login;

import Patients.Patient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides authentication services for patients by verifying user credentials
 * against a CSV file containing patient information.
 */
public class AuthenticationService {

    /**
     * Path to the CSV file containing patient information.
     */
    private String patientCSVFile = "Patient List CSV.csv";

    /**
     * Constructs an AuthenticationService object.
     * The constructor does not perform any specific initialization.
     */
    public AuthenticationService() {
    }

    /**
     * Authenticates a patient by checking the provided user ID and password
     * against the entries in the patient CSV file.
     *
     * @param userID   The user ID of the patient attempting to authenticate.
     * @param password The password of the patient attempting to authenticate.
     * @return A {@link Patient} object if the authentication is successful, or {@code null} if authentication fails.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public Patient authenticate(String userID, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(patientCSVFile))) {
            String line;
            boolean isHeader = true;
            
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header line
                    continue;
                }
                
                String[] data = line.split(",");
                
                if (data.length >= 9) {
                    String csvUserID = data[0].trim();
                    String csvPassword = data[8].trim();

                    if (csvUserID.equals(userID) && csvPassword.equals(password)) {
                        // Create and return a Patient object if authentication succeeds
                        return new Patient(
                                data[0].trim(),  // Patient ID
                                data[1].trim(),  // Name
                                data[2].trim(),  // Date of Birth
                                data[3].trim(),  // Gender
                                data[4].trim(),  // Blood Type
                                data[5].trim(),  // Email
                                data[6].trim(),  // Contact Number
                                data[7].trim(),  // Emergency Contact Number
                                data[8].trim()   // Password
                        );
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Patient CSV file: " + e.getMessage());
        }

        // If no match is found or an error occurs
        DisplayFormat.clearScreen();
        System.out.println("              Authentication failed: Invalid userID or password.");
        return null; 
    }
}
