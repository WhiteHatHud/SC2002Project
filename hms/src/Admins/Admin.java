package Admins;
import Users.Staff;


public class Admin extends Staff{
    public Admin() {
        super();
    }

    public Admin(String staffID, String name, String role, String gender, int age, String officeNumber, String password) {
        super(staffID, name, role, gender, age, officeNumber, password);
    }
}
