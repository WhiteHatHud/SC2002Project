package Admins;

import Login.DisplayManager;
import Utilities.CSVUtilities;
import java.util.Map;

public class AddPharmacist {
    private CSVUtilities csvUtilities;

    public AddPharmacist(String csvFilePath) {
        this.csvUtilities = new CSVUtilities(csvFilePath);
    }

    public void start() {
        DisplayManager.clearScreen();
        

        // Get role count map from CSV and generate a unique Staff ID for the new pharmacist
        Map<String, Integer> roleCountMap = csvUtilities.countRolesAndGenerateID();
        String staffID = csvUtilities.generateNewStaffID("Pharmacist", roleCountMap);
        
        System.out.println("Generated Staff ID for Pharmacist: " + staffID); // Display generated Staff ID

        System.out.print("Enter Name: ");
        String name = AdminShared.getUserInputHandler().getNextLine();

        String role = "Pharmacist";  // Role is fixed as "Pharmacist"
        
        String gender;
        while (true) {
            System.out.print("Enter Gender (M/F): ");
            String genderInput = AdminShared.getUserInputHandler().getNextLine().trim().toUpperCase();

            if (genderInput.equals("M")) {
                gender = "Male";
                break;
            } else if (genderInput.equals("F")) {
                gender = "Female";
                break;
            } else {
                System.out.println("Invalid input. Please enter 'M' for Male or 'F' for Female.");
            }
        }

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(AdminShared.getUserInputHandler().getNextLine());

        System.out.print("Enter Office Number: ");
        String officeNumber = AdminShared.getUserInputHandler().getNextLine();

        // Set the default password
        String password = "password";

        // Create an array of strings to represent the new pharmacist details
        String[] newPharmacistData = {staffID, name, role, gender, String.valueOf(age), officeNumber, password};

        // Use CSVUtilities to add the new pharmacist to the CSV file
        AdminShared.getCSVUpdater().addNewLine(newPharmacistData);

        System.out.println("New Pharmacist added successfully!");
    }
}
