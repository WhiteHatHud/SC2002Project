package Login;

import Admins.Admin;
import Doctors.Doctor;
import Pharmacists.Pharmacist;
import Users.Staff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides authentication services for staff members by verifying user credentials
 * against a CSV file and returning the appropriate Staff type (Pharmacist, Doctor, or Admin)
 * based on the role specified in the CSV data.
 */
public class AuthStaff {

    /**
     * Path to the CSV file that contains staff member data.
     */
    private String csvFilePath;

    /**
     * Constructs an AuthStaff object with the specified path to the CSV file.
     *
     * @param csvFilePath The path to the CSV file containing staff member data.
     */
    public AuthStaff(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * Authenticates a staff member by checking the provided user ID and password
     * against the entries in the CSV file. If authenticated, this method creates
     * and returns a specific staff object (Pharmacist, Doctor, or Admin) based on the role.
     *
     * @param userID   The user ID of the staff member attempting to authenticate.
     * @param password The password of the staff member attempting to authenticate.
     * @return A {@link Staff} object representing the authenticated staff member, or {@code null} if authentication fails.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public Staff authenticateAndRetrieve(String userID, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length > 6 && data[0].trim().equals(userID) && data[6].trim().equals(password)) {

                    String id = data[0].trim();
                    String name = data[1].trim();
                    String role = data[2].trim();
                    String gender = data[3].trim();
                    int age = Integer.parseInt(data[4].trim());
                    String officeNumber = data[5].trim();
                    
                    // Determine and return the appropriate Staff type based on role
                    switch (role.toLowerCase()) {
                        case "pharmacist":
                            return new Pharmacist(id, name, role, gender, age, officeNumber, password);

                        case "doctor":
                            return new Doctor(id, name, role, gender, age, officeNumber, password);

                        case "administrator":
                            return new Admin(id, name, role, gender, age, officeNumber, password);

                        default:
                            System.out.println("Role not recognized: " + role);
                            return null;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        
        // Return null if user not found or password mismatch
        return null;
    }
}
