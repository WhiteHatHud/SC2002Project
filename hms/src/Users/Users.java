/**
 * Base class representing a user with an ID, name, and password.
 */
package Users;

public class Users {
    protected String userID;
    protected String name;
    protected String password; // Password field in Users

    /**
     * Default constructor for the Users class.
     */
    public Users() {}

    /**
     * Constructor to create a user with a specific ID and name.
     *
     * @param userID The unique identifier for the user.
     * @param name The name of the user.
     */
    public Users(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    /**
     * Retrieves the user ID.
     *
     * @return The user ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
