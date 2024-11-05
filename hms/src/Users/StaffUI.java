package Users;

import java.util.List;

public class StaffUI extends StaffData{
    StaffData dataInterface = new StaffData();
    public StaffUI(){}

    public void displayAllStaff(){
        List<Staff> staffList = dataInterface.getAllStaff();
        for (Staff staff : staffList){
            print(staff);
        }
    }
    public void print(Staff staff){
        System.out.println(
            staff.userID + ", " +
            staff.name + ", " +
            staff.role + ", " +
            staff.gender + ", " +
            staff.age
        );
    }
    public void deleteStaff(String staffID){
        if (dataInterface.removeStaffByID(staffID)){
            System.out.println(staffID + " deleted from Staff List");
            return;
        }
        System.out.println("Unable to find staff with matching ID");
    }
    public void checkStaff(String staffID) {
        Staff staff = dataInterface.getStaffByID(staffID);
        if (staff != null) {
            System.out.println("Staff information:");
            System.out.println("ID: " + staff.getUserID());
            System.out.println("Name: " + staff.getName());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Gender: " + staff.getGender());
            System.out.println("Age: " + staff.getAge());
            System.out.println("Office Number: " + staff.getOfficeNumber());
            return;
        }
        System.out.println("Unable to find staff with matching ID");
    }
    

}
