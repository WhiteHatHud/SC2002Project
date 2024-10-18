package Login;

public class AuthenticationService {
    public boolean authenticate(String userID, String password) {

        // This is a placeholder,
        if (userID.equals("patient123") && password.equals("password123")) {
            return true;
        } else {
            return false;
        }
    }
}
