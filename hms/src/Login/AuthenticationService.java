package Login;

import Patients.Patient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthenticationService {
    private String patientCSVFile = "Patient List CSV.csv"; // Path to the CSV file

    public AuthenticationService() {
        // Constructor can be empty or used for any initialization needed
    }

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
                if (data.length >= 9) { //
                    String csvUserID = data[0].trim();
                    String csvPassword = data[8].trim();

                    if (csvUserID.equals(userID) && csvPassword.equals(password)) {
                        // Create a Patient object if authentication succeeds
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
