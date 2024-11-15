package Login;

import Users.Users;
import java.util.List;

/**
 * Interface for managing a registry of users. Provides methods to add users,
 * retrieve all users, and find a user by their unique ID.
 *
 * @param <T> The type of user that extends {@link Users} and is managed by this registry.
 */
public interface UserRegistry<T extends Users> {

    /**
     * Adds a user to the registry.
     *
     * @param user The user to add.
     */
    void addUser(T user);

    /**
     * Retrieves a list of all users in the registry.
     *
     * @return A {@link List} of all users.
     */
    List<T> getAllUsers();

    /**
     * Finds a user by their unique ID.
     *
     * @param userID The ID of the user to find.
     * @return The user with the specified ID, or {@code null} if no such user exists.
     */
    T findUserByID(String userID);
}
