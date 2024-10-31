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
    public void checkStaff(String staffID){
        Staff staff;
        if((staff = dataInterface.getStaffByID(staffID)) != null){
            print(staff);
            return;
        }
        System.out.println("Unable to find staff with matching ID");
    }
    public static void main(String[] args) {
        StaffUI ui = new StaffUI();
        ui.checkStaff("D002");
    }
}
