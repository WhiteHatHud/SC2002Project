package Pharmacists;
import Users.Staff;

public class Pharmacist extends Staff {

    public Pharmacist() {
        super();
    }

    public Pharmacist(String staffID, String name, String gender, int age, String officeNumber) {
        super(staffID, name, "Pharmacist", gender, age, officeNumber);
    }

}
