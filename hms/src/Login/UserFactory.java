package Login;
import Patients.Patient;
import Users.Users;

public class UserFactory {

    public static Users createUser(String userType, String[] data) {
        switch (userType.toLowerCase()) {
            case "patient":
                return new Patient(data[0], data[1], data[2], data[3], data[4], data[5]);
            // Add other types as needed
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}
