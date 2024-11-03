package Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthStaff {
    private String csvFilePath;

    public AuthStaff(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public boolean authenticateUser(String userID, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip the header row

            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); 

                if (data.length > 1 && data[0].equals(userID) && data[6].equals(password)) {
                    return true; // User and password match
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return false; // User not found or password mismatch
    }
}
