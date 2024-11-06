package Admins;
import Doctors.Doctor;
import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Pharmacists.Pharmacist;
import Users.*;
import Utilities.LogoutTimer;
import appt.AdminUI;

import java.util.*;


public class AdminController implements ControllerInt {
    private final Admin admin;
    StaffData staffData = new StaffData();
    
    public AdminController(Admin admin) {
        this.admin = admin;
       
    }

    public boolean start() {
        System.out.println("Welcome, " + admin.getName());

        boolean isActive = true;
        while (isActive) {
            AdminShared.getDisplayManager().displayMenu(); 
            int choice = AdminShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice);
        }

        return false; 
    }

    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1: 
                viewAndManageStaff();
                break;
            case 2: 
                AdminUI ui = new AdminUI();
                ui.start();
                DisplayManager.clearScreen();
                break;
            case 3: 
                ManageMedAction manage = new ManageMedAction();
                manage.start();
                break;
            case 4:
                LogoutTimer.confirmLogout(); 
                return false; 
            default:
                //Login.DisplayManager.invalidChoice();
                break;
        }
        return true; // Continue session if choice is not logout
    }

    private void viewAndManageStaff() {
        boolean managingStaff = true;
        while (managingStaff) {
            AdminShared.getDisplayManager().getViewAndManageStaffMenu();
            int staffChoice = AdminShared.getUserInputHandler().getUserChoice();
            switch (staffChoice) {
                case 1:
                    manageDoctors();
                    break;
                case 2:
                    managePharmacists();
                    break;
                case 3:
                    displayAllStaff();
                    break;
                case 4:
                    managingStaff = false;
                default:
                    //Login.DisplayManager.invalidChoice();
                    break;
            }
        }
    }

    private void manageDoctors() {
        boolean managingDoctors = true;
        while (managingDoctors) {
            AdminShared.getDisplayManager().getManageDoctors();
            int doctorChoice = AdminShared.getUserInputHandler().getUserChoice();
            String role = "Doctor";

            switch (doctorChoice) {
                case 1:
                    addStaffMember(role);
                    break;
                case 2:
                    updateStaffInfo(role);
                    break;
                case 3:
                    removeStaff();
                    break;
                case 4:
                    managingDoctors = false;
                default:
                    //Login.DisplayManager.invalidChoice();
                    break;
            }
        }
    }

    private void managePharmacists() {
        boolean managingPharmacists = true;
        while (managingPharmacists) {
            AdminShared.getDisplayManager().getManagePharma();
            int pharmacistChoice = AdminShared.getUserInputHandler().getUserChoice();
            String role = "Pharmacist";
            switch (pharmacistChoice) {
                case 1:
                    addStaffMember(role);
                    break;
                case 2:
                    updateStaffInfo(role);
                    break;
                case 3:
                    removeStaff();
                    break;
                case 4:
                    managingPharmacists = false;
                    break;
                default:
                    //Login.DisplayManager.invalidChoice();
                    break;
            }
        }
    }

    
    private void displayAllStaff() { 
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

        StaffData staffData = new StaffData();
        List<Staff> staffList = staffData.getAllStaff();
        

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



    private void addStaffMember(String role) {
        DisplayManager.clearScreen();
        System.out.println("Adding a new " + role.toLowerCase() + "...");

        // Generate staff ID based on the role
        Map<String, Integer> roleCountMap = staffData.countRolesAndGenerateID();
        String staffID = staffData.generateNewStaffID(role, roleCountMap);

        System.out.println("The Assigned " + role + "'s ID is: " + staffID);

        
        System.out.print("Enter " + role + "'s Name: ");
        String name = AdminShared.getUserInputHandler().getNextLine();

        System.out.print("Enter " + role + "'s Gender (M/F): ");
        String gender = AdminShared.getUserInputHandler().getNextLine();

        int age;
        while (true) {
            System.out.print("Enter " + role + "'s Age: ");
            try {
                age = AdminShared.getUserInputHandler().getUserChoice();
                if (age > 0) break;
                else System.out.println("Age must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid age.");
            }
        }   

        System.out.print("Enter " + role + "'s Office Number: ");
        String officeNumber = AdminShared.getUserInputHandler().getNextLine();

        String password = "password";

        Staff staff;
        if (role.equalsIgnoreCase("Doctor")) {
            staff = new Doctor(staffID, name, role, gender, age, officeNumber,password);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            staff = new Pharmacist(staffID, name, role, gender, age, officeNumber,password);
        } else {
            System.out.println("Invalid role specified.");
            return;
        }


        if (staffData.addStaff(staff)) {
            System.out.println(role + " added successfully with ID: " + staffID);
        }
    }

    
    private void updateStaffInfo(String role) {
        System.out.println("Updating " + role.toLowerCase() + " information...");
        System.out.print("Enter " + role + " ID to update: ");
        String staffID = AdminShared.getUserInputHandler().getInput();
    

        String idPrefix = role.equalsIgnoreCase("Doctor") ? "D" : "P";
        if (!staffID.startsWith(idPrefix)) {
            System.out.println("Error: Invalid " + role + " ID. " + role + " IDs must start with '" + idPrefix + "'.");
            return;
        }
    
        // Check if the ID exists in the CSV file
        if (!AdminShared.getCSVUtilities().checkIfUserExists(staffID)) {
            System.out.println(role + " ID not found.");
            return;
        }
    
        boolean continueUpdating = true;
        while (continueUpdating) {
            System.out.println("\nWhich field would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Gender");
            System.out.println("3. Age");
            System.out.println("4. Office Number");
            System.out.println("5. Password");
            System.out.println("6. Finish updating");
            System.out.print("Enter the number of the field to update: ");
    
            int choice = AdminShared.getUserInputHandler().getUserChoice();
    
            String fieldName = null;
            switch (choice) {
                case 1:
                    fieldName = "Name";
                    break;

                case 2:
                    fieldName = "Gender";
                    break;

                case 3:
                    fieldName = "Age";
                    break;

                case 4:
                    fieldName = "Office Number";
                    break;

                case 5:
                    fieldName = "Password";
                    break;

                case 6:
                    continueUpdating = false; // Exit the loop
                    System.out.println("Finished updating.");
                    continue; 

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    continue; 
            }
    
            if (fieldName != null) {
                System.out.print("Enter new value for " + fieldName + ": ");
                String newValue = AdminShared.getUserInputHandler().getNextLine();
    
                // Update the specified field for the given Staff ID
                AdminShared.getCSVUpdater().updateField(staffID, fieldName, newValue);
                System.out.println(fieldName + " updated successfully for " + role + " ID " + staffID + ".");
            }
        }
    }

    public void removeStaff() {
        System.out.print("Enter Staff ID to remove: ");
        String staffID = AdminShared.getUserInputHandler().getNextLine();
    
        StaffUI staffUI = new StaffUI();

        staffUI.checkStaff(staffID);

        // Confirm deletion
        System.out.print("Are you sure you want to delete Staff ID " + staffID + "? (yes/no): ");
        String confirmation = AdminShared.getUserInputHandler().getNextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }
    

        if (staffData.removeStaffByID(staffID)) {
            System.out.println("Staff with ID " + staffID + " has been successfully removed.");
        } else {
            System.out.println("Error: Staff removal failed.");
        }
    }
    

    


}
