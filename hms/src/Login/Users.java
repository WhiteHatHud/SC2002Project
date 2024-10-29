package Login;

public abstract class Users {
    // Attributes
    protected String userID;
    protected String password;
    protected String name;
    //protected String role; only for Staf class
    //protected String dob; dob should only be for patient class
    public Users(){
    }

    public Users(String userID, String name) {
        this.userID = userID;
        this.name = name;
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

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    
}


