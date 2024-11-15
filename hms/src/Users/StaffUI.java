/**
 * A class for managing and displaying staff information in the system.
 * Extends the StaffData class and provides user interface methods for interacting with staff data.
 */
package Users;

import java.util.List;

public class StaffUI extends StaffData {
    StaffData dataInterface = new StaffData();

    /**
     * Default constructor for StaffUI.
     */
    public StaffUI() {}

    /**
     * Displays all staff members by retrieving the list from the data interface and printing each.
     */
    public void displayAllStaff() {
        List<Staff> staffList = dataInterface.getAllStaff();
        for (Staff staff : staffList) {
            print(staff);
        }
    }

    /**
     * Prints the details of a specific staff member.
     *
     * @param staff The staff member to print details for.
     */
    public void print(Staff staff) {
        System.out.println(
            staff.userID + ", " +
            staff.name + ", " +
            staff.role + ", " +
            staff.gender + ", " +
            staff.age
        );
    }

    /**
     * Deletes a staff member from the staff list based on the given ID.
     *
     * @param staffID The ID of the staff member to delete.
     */
    public void deleteStaff(String staffID) {
        if (dataInterface.removeStaffByID(staffID)) {
            System.out.println(staffID + " deleted from Staff List");
            return;
        }
        System.out.println("Unable to find staff with matching ID");
    }

    /**
     * Checks if a staff member with the given ID exists and displays their details if found.
     *
     * @param staffID The ID of the staff member to check.
     */
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
