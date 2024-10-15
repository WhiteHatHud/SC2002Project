package hms.users;

public abstract class Users {
    // Attributes
    protected String userID;
    protected String password;
    protected String name;
    protected String role;    
    
    // userID is the first col in excel
    public Users(String userID, String name, String role) {
        this.userID = userID;
        this.name = name;
        this.role = role;
        this.password = "password";  // Default password
    }
    
    // Getters and Setters
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    
    // For role specific menu
    public abstract void displayMenu();
}
