package Login;

import Patients.Patient;
import Users.Users;

/**
 * Factory class for creating instances of {@link Users} based on the specified user type.
 * This class uses a factory design pattern to centralize the creation of different user types.
 */
public class UserFactory {

    /**
     * Creates a {@link Users} object based on the specified user type and data.
     * Currently supports the "patient" user type. Additional user types can be added as needed.
     *
     * @param userType The type of user to create (e.g., "patient").
     * @param data     An array of Strings containing the user data fields. The specific fields
     *                 required depend on the user type.
     * @return A {@link Users} instance of the specified type, populated with the provided data.
     * @throws IllegalArgumentException if the user type is unknown or unsupported.
     */
    public static Users createUser(String userType, String[] data) {
        switch (userType.toLowerCase()) {
            case "patient":
                return new Patient(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
            // Add other types as needed
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}
