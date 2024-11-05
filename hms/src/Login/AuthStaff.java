package Login;
import Admins.Admin;
import Doctors.Doctor;
import Pharmacists.Pharmacist;
import Users.Staff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthStaff {
    private String csvFilePath;

    public AuthStaff(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public Staff authenticateAndRetrieve(String userID, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length > 6 && data[0].trim().equals(userID) && data[6].trim().equals(password)) {

                    String id = data[0].trim();
                    String name = data[1].trim();
                    String role = data[2].trim();
                    String gender = data[3].trim();
                    int age = Integer.parseInt(data[4].trim());
                    String officeNumber = data[5].trim();

                    switch (role.toLowerCase()) {
                        case "pharmacist":
                            return new Pharmacist(id, name, gender, age,officeNumber);
                        case "doctor":
                            return new Doctor(id, name, role, gender, age, officeNumber);
                        case "administrator":
                            return new Admin(id, name, role, gender, age, officeNumber);
                        default:
                            System.out.println("Role not recognized: " + role);
                            return null;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return null; // User not found or password mismatch
    }
}
