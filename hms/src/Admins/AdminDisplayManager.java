package Admins;

import Login.DisplayManager;

/**
 * The AdminDisplayManager class provides methods to display various menus and options specific to
 * the Admin interface in the hospital management system. This class extends DisplayManager to
 * inherit common display functionalities and adds methods tailored to the administrative functions.
 */
public class AdminDisplayManager extends DisplayManager {

    /**
     * Displays the main admin menu with options for managing staff, appointments, medication
     * inventory, patient registration, and logout.
     *
     * @param name The name of the admin to be displayed in the welcome message.
     */
    public void displayMenu(String name) {
        DisplayManager.clearScreen(); 
        System.out.println("Welcome, Admin " + name);
        System.out.println("1. View and Manage Staffs");
        System.out.println("2. Manage Appointment Details");
        System.out.println("3. Manage Medication Inventory");
        System.out.println("4. Register a New Patient");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
    }

    /**
     * Displays the menu for viewing and managing hospital staff. This includes options to manage
     * doctors, manage pharmacists, display all staff, or return to the main menu.
     */
    public void getViewAndManageStaffMenu() {
        DisplayManager.clearScreen();
        System.out.println("=== View and Manage Hospital Staff ===");
        System.out.println("1. Manage Doctors");
        System.out.println("2. Manage Pharmacists");
        System.out.println("3. Display all Staffs");
        System.out.println("4. Return to Main Menu");
        System.out.print("Please enter your choice: ");
    }

    /**
     * Displays the menu for managing doctors. This includes options to add a new doctor,
     * update doctor information, remove a doctor, or return to the main menu.
     */
    public void getManageDoctors() {
        DisplayManager.clearScreen();
        System.out.println("1. Add a New Doctor");
        System.out.println("2. Update Doctor Information");
        System.out.println("3. Remove a Doctor");
        System.out.println("4. Return to Main Menu");
        System.out.print("Please enter your choice: ");
    }

    /**
     * Displays the menu for managing pharmacists. This includes options to add a pharmacist,
     * update pharmacist information, remove a pharmacist, or return to the main menu.
     */
    public void getManagePharma() {
        DisplayManager.clearScreen();
        System.out.println("1. Add Pharmacist");
        System.out.println("2. Update Pharmacist");
        System.out.println("3. Remove Pharmacist");
        System.out.println("4. Return to Main Menu");
        System.out.print("Please enter your choice: ");
    }

    /**
     * Displays the medication inventory management menu. This includes options to view
     * the inventory, add new medications, update stock levels, remove medications,
     * update low stock alert levels, approve replenishment requests, and return to the main menu.
     */
    public void manageMedicationInvetoryMenu() {
        DisplayManager.clearScreen();
        System.out.println("=== Medication Inventory Management ===");
        System.out.println("1. View Medication Inventory");
        System.out.println("2. Add New Medication");
        System.out.println("3. Update Medication Stock Level");
        System.out.println("4. Remove Medication");
        System.out.println("5. Update Low Stock Alert Level");
        System.out.println("6. Approve Replenishment Requests");
        System.out.println("7. Return to Main Menu");
        System.out.print("Please enter your choice: ");
    }
}
