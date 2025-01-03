package Admins;
import Doctors.Doctor;
import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Patients.Patient;
import Patients.PatientRegistry;
import Pharmacists.Pharmacist;
import Users.*;
import Utilities.LogoutTimer;
import appt.AdminUI;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


public class AdminController implements ControllerInt {
    private Admin admin;
    StaffData staffData = new StaffData();
    
    public AdminController(Admin admin) {
        this.admin = admin;
       
    }

    public boolean start() {

        String name = admin.getName();

        boolean isActive = true;
        while (isActive) {
            AdminShared.getDisplayManager().displayMenu(name); 

            int choice = AdminShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice);
        }

        return false; 
    }

    @Override
    public boolean handleChoice(int choice) {
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
                registerNewPatient();
                break;
            case 5: // Logout
                if (LogoutTimer.confirmLogout()) {
                    return false; // Ends the session only if logout is confirmed
                } else {
                    DisplayManager.clearScreen();
                    
                    return true; // Continue the session without printing additional messages
                }
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
                    break;
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
                    removeStaff(role); // Pass "Doctor" as the role
                    break;
                case 4:
                    managingDoctors = false;
                    break;
                default:
                    // Login.DisplayManager.invalidChoice();
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
            DisplayManager.clearScreen();
            switch (pharmacistChoice) {
                case 1:
                    addStaffMember(role);
                    break;
                case 2:
                    updateStaffInfo(role);
                    break;
                case 3:
                    removeStaff(role); // Pass "Pharmacist" as the role
                    break;
                case 4:
                    managingPharmacists = false;
                    break;
                default:
                    // Login.DisplayManager.invalidChoice();
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
            case 2: // Sort by Name (case-insensitive)
                comparator = Comparator.comparing(staff -> staff.getName().toLowerCase());
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
    
        // Display formatted header
        System.out.printf("%-10s %-20s %-15s %-10s %-5s%n", "ID", "Name", "Role", "Gender", "Age");
        System.out.println("---------------------------------------------------------------");
    
        // Display sorted list
        for (Staff staff : staffList) {
            System.out.printf("%-10s %-20s %-15s %-10s %-5d%n", 
                              staff.getUserID(), 
                              staff.getName(), 
                              staff.getRole(), 
                              staff.getGender(), 
                              staff.getAge());
        }
        DisplayManager.pauseContinue();
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
    
        // Gender selection
        String gender = "";
        boolean validGender = false;
        System.out.println("Select Gender:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        while (!validGender) {
            int genderChoice = AdminShared.getUserInputHandler().getUserChoice();
            if (genderChoice == 1) {
                gender = "Male";
                validGender = true;
            } else if (genderChoice == 2) {
                gender = "Female";
                validGender = true;
            } else {
                System.out.println("Invalid choice. Please select 1 for Male or 2 for Female.");
            }
        }
    
        int age;
        while (true) {
            System.out.print("Enter " + role + "'s Age: ");
            try {
                age = AdminShared.getUserInputHandler().getUserChoice();
                if (age > 0) break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid age.");
            }
        }
    
        System.out.print("Enter " + role + "'s Office Number: ");
        String officeNumber = AdminShared.getUserInputHandler().getNextLine();
    
        String password = "password";
    
        Staff staff;
        if (role.equalsIgnoreCase("Doctor")) {
            staff = new Doctor(staffID, name, role, gender, age, officeNumber, password);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            staff = new Pharmacist(staffID, name, role, gender, age, officeNumber, password);
        } else {
            System.out.println("Invalid role specified.");
            return;
        }
        
        if (staffData.addStaff(staff)) {
            System.out.println(role + " added successfully with ID: " + staffID);
            System.out.print("Press any key to continue...");
            Scanner scanner=new Scanner(System.in);
            scanner.nextLine(); // Wait for user to press any key
            DisplayManager.clearScreen();
        }
    }
    
    private void updateStaffInfo(String role) {
        System.out.println("Updating " + role.toLowerCase() + " information...");
        System.out.print("Enter " + role + " ID to update: ");
        String staffID = AdminShared.getUserInputHandler().getInput();
    
        String idPrefix = role.equalsIgnoreCase("Doctor") ? "D" : "P";
        if (!staffID.startsWith(idPrefix)) {
            System.out.println("Error: Invalid " + role + " ID. " + role + " IDs must start with '" + idPrefix + "'.");
            DisplayManager.pauseContinue();
            return;
        }
    
        // Check if the ID exists in the CSV file
        if (!AdminShared.getCSVUtilities().checkIfUserExists(staffID)) {
            System.out.println(role + " ID not found.");
            DisplayManager.pauseContinue();
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
            String newValue = "";
    
            switch (choice) {
                case 1:
                    fieldName = "Name";
                    System.out.print("Enter new value for Name: ");
                    newValue = AdminShared.getUserInputHandler().getNextLine().trim();
                    break;
                case 2:
                    fieldName = "Gender";
                    System.out.println("Select new gender:");
                    System.out.println("1. Male");
                    System.out.println("2. Female");
                    boolean validGender = false;
                    while (!validGender) {
                        int genderChoice = AdminShared.getUserInputHandler().getUserChoice();
                        if (genderChoice == 1) {
                            newValue = "Male";
                            validGender = true;
                        } else if (genderChoice == 2) {
                            newValue = "Female";
                            validGender = true;
                        } else {
                            System.out.println("Invalid choice. Please select 1 for Male or 2 for Female.");
                        }
                    }
                    break;
                case 3:
                    fieldName = "Age";
                    boolean validAge = false;
                    while (!validAge) {
                        System.out.print("Enter new value for Age: ");
                        try {
                            int age = AdminShared.getUserInputHandler().getUserChoice();
                            if (age > 0) {
                                newValue = String.valueOf(age);
                                validAge = true;
                            } else {
                                System.out.println("Age must be a positive number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid age.");
                        }
                    }
                    break;
                case 4:
                    fieldName = "Office Number";
                    System.out.print("Enter new value for Office Number: ");
                    newValue = AdminShared.getUserInputHandler().getNextLine().trim();
                    break;
                case 5:
                    fieldName = "Password";
                    System.out.print("Enter new value for Password: ");
                    newValue = AdminShared.getUserInputHandler().getNextLine().trim();
                    break;
                case 6:
                    continueUpdating = false; // Exit the loop
                    DisplayManager.clearScreen();
                    System.out.println("Finished updating.");
                    continue;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    continue;
            }
    
            if (fieldName != null && !newValue.isEmpty()) {
                // Update the specified field for the given Staff ID
                AdminShared.getCSVUpdater().updateField(staffID, fieldName, newValue);
                System.out.println(fieldName + " updated successfully for " + role + " ID " + staffID + ".");
    
                // If the name was updated, propagate the change to all relevant CSVs
                if (fieldName.equals("Name")) {
                    AdminShared.getCSVUtilities().updateDoctorNameInAllCSVs(staffID, newValue);
                }
            }
        }
    }
    
    

    public void removeStaff(String role) {
        System.out.print("Enter Staff ID to remove: ");
        String staffID = AdminShared.getUserInputHandler().getNextLine();
    
        // Check if the staff ID exists
        if (!AdminShared.getCSVUtilities().checkIfUserExists(staffID)) {
            DisplayManager.clearScreen(); 
            System.out.println("Invalid staff! Operation aborted.");
            DisplayManager.pauseContinue();
            return; // Return to the menu if the ID is invalid
        }
    
        // Validate the role of the staff ID
        Staff staff = staffData.getStaffByID(staffID);
        if (staff == null || !staff.getRole().equalsIgnoreCase(role)) {
            DisplayManager.clearScreen();
            System.out.println("Error: The entered ID does not belong to a " + role + ". Operation aborted.");
            DisplayManager.pauseContinue();
            return;
        }
    
        StaffUI staffUI = new StaffUI();
        staffUI.checkStaff(staffID);
    
        // Confirm deletion
        System.out.print("Are you sure you want to delete Staff ID " + staffID + "? (yes/no): ");
        String confirmation = AdminShared.getUserInputHandler().getNextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled. Returning to the menu.");
            return;
        }
    
        if (staffData.removeStaffByID(staffID)) {
            DisplayManager.clearScreen(); 
            System.out.println("Staff with ID " + staffID + " has been successfully removed.");
        } 
    }
    
    

public void registerNewPatient() {
    DisplayManager.clearScreen();
    System.out.println("=== Input Patient Information ===");

    String[] patientData = new String[9];

    // Collect patient information and store in the array
    patientData[0] = AdminShared.getCSVUtilitiesPatient().generateLatestPatientID();
    System.out.println("Patient is assigned ID: " + patientData[0]);

    System.out.print("Enter Name: ");
    patientData[1] = AdminShared.getUserInputHandler().getNextLine().trim();

    // Validate date of birth input
    boolean validDate = false;
    while (!validDate) {
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dobInput = AdminShared.getUserInputHandler().getNextLine().trim();
        try {
            LocalDate.parse(dobInput);
            patientData[2] = dobInput;
            validDate = true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter a valid date (YYYY-MM-DD).");
        }
    }

    // Gender selection
    String[] genders = {"1. Male", "2. Female"};
    System.out.println("Select Gender:");
    for (String genderOption : genders) {
        System.out.println(genderOption);
    }
    boolean validGender = false;
    while (!validGender) {
        int genderChoice = AdminShared.getUserInputHandler().getUserChoice();
        if (genderChoice == 1) {
            patientData[3] = "Male";
            validGender = true;
        } else if (genderChoice == 2) {
            patientData[3] = "Female";
            validGender = true;
        } else {
            //System.out.println("Invalid choice. Please select 1 for Male or 2 for Female.");
        }
    }

    // Blood type selection
    String[] bloodTypes = {"1. A+", "2. A-", "3. B+", "4. B-", "5. AB+", "6. AB-", "7. O+", "8. O-"};
    System.out.println("Select Blood Type:");
    for (String bloodTypeOption : bloodTypes) {
        System.out.println(bloodTypeOption);
    }
    boolean validBloodType = false;
    while (!validBloodType) {
        int bloodTypeChoice = AdminShared.getUserInputHandler().getUserChoice();
        if (bloodTypeChoice >= 1 && bloodTypeChoice <= bloodTypes.length) {
            patientData[4] = bloodTypes[bloodTypeChoice - 1].substring(3); // Extract the blood type (e.g., "A+" from "1. A+")
            validBloodType = true;
        } else {
            //System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    System.out.print("Enter Email: ");
    patientData[5] = AdminShared.getUserInputHandler().getNextLine().trim();

    System.out.print("Enter Contact Number: ");
    patientData[6] = AdminShared.getUserInputHandler().getNextLine().trim();

    System.out.print("Enter Emergency Contact Number: ");
    patientData[7] = AdminShared.getUserInputHandler().getNextLine().trim();

    System.out.println("Password Will Be Set to The Default Password");
    patientData[8] = "password";

    AdminShared.getCSVUpdaterPatient().addNewPatient(patientData);

    Patient newPatient = new Patient(
        patientData[0], // Patient ID
        patientData[1], // Name
        patientData[2], // Date of Birth
        patientData[3], // Gender
        patientData[4], // Blood Type
        patientData[5], // Email
        patientData[6], // Contact Number
        patientData[7], // Emergency Contact Number
        patientData[8]  // Password
    );
    PatientRegistry pR = new PatientRegistry();
    pR.addUser(newPatient);

    System.out.println("Patient registered successfully!");
}

}