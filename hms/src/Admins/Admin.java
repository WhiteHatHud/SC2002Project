package Admins;
import Login.Users;

public class Admin extends Users{
    public Admin(String adminID, String name, String password){
       userID = adminID;
       this.name = name;
       role = "admin";
       password = "admin";
    }
}
