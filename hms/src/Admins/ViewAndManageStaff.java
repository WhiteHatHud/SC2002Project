package Admins;
import Login.DisplayManager;
import Users.Staff;
import java.util.*;


public class ViewAndManageStaff implements MenuAction {


    public ViewAndManageStaff() {

    }

    @Override
    public void execute() {
        DisplayManager.clearScreen();
        System.out.println("=== View and Manage Hospital Staff ===");
        System.out.println("1. Manage Doctors");
        System.out.println("2. Manage Pharmacists");
        System.out.println("3. Display all Staffs");
        System.out.print("Choose an option: ");
        int choice = AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:
                manageDoctors();
                break;
            case 2:
                managePharmacists();
                break;
            case 3:
                displayStaffs();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }


    private List<Staff> getStaffList() 
    {
        
        List<Staff> staffList = AdminShared.getCSVUtilities().readStaffList(); 

        if (staffList == null) {
            staffList = new ArrayList<>(); 
        }
        return staffList;
    }

    private void displayStaffs() { 
        DisplayManager.clearScreen();
        System.out.println("Choose sorting criteria:");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. Role");
        System.out.println("4. Gender");
        System.out.println("5. Age");
        System.out.print("Choose an attribute to sort by: ");
        int attributeChoice = AdminShared.getUserInputHandler().getUserChoice();
        
        System.out.println("Choose sorting order:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Choose sorting order: ");
        int orderChoice = AdminShared.getUserInputHandler().getUserChoice();

        // Retrieve staff list
        List<Staff> staffList = getStaffList();
        
        // Define comparator based on attribute choice
        Comparator<Staff> comparator = null;
        switch (attributeChoice) {
            case 1: // Sort by ID
                comparator = Comparator.comparing(Staff::getUserID);
                break;
            case 2: // Sort by Name
                comparator = Comparator.comparing(Staff::getName);
                break;
            case 3: // Sort by Role
                comparator = Comparator.comparing(Staff::getRole);
                break;
            case 4: // Sort by Gender
                comparator = Comparator.comparing(Staff::getGender);
                break;
            case 5: // Sort by Age
                comparator = Comparator.comparingInt(Staff::getAge);
                break;
            default:
                System.out.println("Invalid attribute choice.");
                return;
        }

        // Apply descending order if selected
        if (orderChoice == 2) {
            comparator = comparator.reversed();
        }

        // Sort the list
        Collections.sort(staffList, comparator);

        // Display sorted list
        System.out.println("Sorted Staff List: \n");
        for (Staff staff : staffList) {
            System.out.println(staff);
        }
    }

    private void manageDoctors() {
        DisplayManager.clearScreen();
        System.out.println("1. Add a New Doctor");
        System.out.println("2. Update Doctor Information");
        System.out.println("3. Remove a Doctor");
        System.out.print("Choose an option: ");
        int choice =AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:
            AddDoctor addDoctor = new AddDoctor("Staff_List.csv"); 
            addDoctor.start(); 
                break;
            case 2:
            UpdateDoctor updateDoctor = new UpdateDoctor("Staff_List.csv"); 
            updateDoctor.start(); 
                break;
            case 3:
            RemoveDoctor removeDoctor = new RemoveDoctor("Staff_List.csv","appointments.csv");
            removeDoctor.start();
            break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void managePharmacists() {
        DisplayManager.clearScreen();
        System.out.println("1. Add Pharmacist");
        System.out.println("2. Update Pharmacist");
        System.out.println("3. Remove Pharmacist");
        System.out.print("Choose an option: ");
        int choice = AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:
            AddPharmacist addPharma = new AddPharmacist("Staff_List.csv"); 
            addPharma.start(); 
                break;
            case 2:
            UpdatePharmacist updatePharma = new UpdatePharmacist("Staff_List.csv"); 
            updatePharma.start(); 
                break;
            case 3:
            RemovePharmacist removePharma = new RemovePharmacist("Staff_List.csv");
            removePharma.start();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    @Override
    public String getDescription() {
        return "View and Manage Hospital Staff";
    }
}

