package Admins;
import Login.DisplayManager;

public class AdminDisplayManager extends DisplayManager {

    public void displayMenu(){
        //DisplayManager.clearScreen();
        System.out.println("1. View and Manage Staffs");
        System.out.println("2. Manage Appointment Details");
        System.out.println("3. Manage Medication Inventory");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

    }

    public void getViewAndManageStaffMenu(){
        //DisplayManager.clearScreen();
        System.out.println("=== View and Manage Hospital Staff ===");
        System.out.println("1. Manage Doctors");
        System.out.println("2. Manage Pharmacists");
        System.out.println("3. Display all Staffs");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void getManageDoctors(){
        //DisplayManager.clearScreen();
        System.out.println("1. Add a New Doctor");
        System.out.println("2. Update Doctor Information");
        System.out.println("3. Remove a Doctor");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void getManagePharma(){
        //DisplayManager.clearScreen();
        System.out.println("1. Add Pharmacist");
        System.out.println("2. Update Pharmacist");
        System.out.println("3. Remove Pharmacist");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");
    }
    public void manageMedicationInvetoryMenu(){



    }

    
}
