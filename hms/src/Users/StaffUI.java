package Users;

import java.util.List;

public class StaffUI extends StaffData{
    StaffData dataInterface = new StaffData();
    public StaffUI(){}

    public void displayAllStaff(){
        List<Staff> staffList = dataInterface.getAllStaff();
        for (Staff temp : staffList){
            print(temp);
        }
    }
    public void print(Staff temp){
        System.out.println(
            temp.userID + ", " +
            temp.name + ", " +
            temp.role + ", " +
            temp.gender + ", " +
            temp.age
        );
    }
    public static void main(String[] args) {
        StaffUI ui = new StaffUI();
        ui.displayAllStaff();
    }
}
