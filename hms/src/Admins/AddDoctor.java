package Admins;

import Login.DisplayManager;
import Utilities.CSVUtilities;
import java.util.Map;

public class AddDoctor {
    private CSVUtilities csvUtilities;
    private String appointmentCsvFilePath;

    public AddDoctor(String doctorCsvFilePath, String appointmentCsvFilePath) {
        this.csvUtilities = new CSVUtilities(doctorCsvFilePath);
        this.appointmentCsvFilePath = appointmentCsvFilePath;
    }

    public void start() {
        DisplayManager.clearScreen();

 
        Map<String, Integer> roleCountMap = csvUtilities.countRolesAndGenerateID();
        String staffID = csvUtilities.generateNewStaffID("Doctor", roleCountMap);
        
        System.out.println("Generated Staff ID for Doctor: " + staffID); 

        System.out.print("Enter Name: ");
        String name = AdminShared.getUserInputHandler().getNextLine();

        String role = "Doctor";  
        
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


        String[] newDoctorData = {staffID, name, role, gender, String.valueOf(age), officeNumber, password};

        // Add the doctor to the doctor CSV file
        AdminShared.getCSVUpdater().addNewLine(newDoctorData);
        System.out.println("New Doctor added successfully!");


    }

}
