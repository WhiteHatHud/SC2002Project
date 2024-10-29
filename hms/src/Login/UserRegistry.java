package Login;
import Users.Users;
import java.util.List;

public interface UserRegistry<T extends Users> {
    void addUser(T user);           // Add user
    List<T> getAllUsers();          // Get all users
    T findUserByID(String userID);  // Find user by ID
}
