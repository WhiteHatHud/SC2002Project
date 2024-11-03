package Pharmacists;
import Users.Staff;

public class Pharmacist extends Staff {

    public Pharmacist() {
        super();
    }

    public Pharmacist(String staffID, String name, String role, String gender, int age, String officeNumber) {
        super(staffID, name, role, gender, age, officeNumber);
    }

}
