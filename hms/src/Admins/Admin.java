package Admins;
import Users.*;

public class Admin extends Staff{
    public Admin(String adminID, String name, String gender, int age){
        super(adminID, name, "admin", gender, age);
    }
}
