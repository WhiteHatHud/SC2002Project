package Utilities;

import Users.Staff;
import java.io.*;
import java.util.*;

public class CSVUtilities {
    private String csvFile;

    public CSVUtilities(String csvFile) {
        this.csvFile = csvFile;
    }

    // Method to read CSV and count roles
    public Map<String, Integer> countRolesAndGenerateID() {
        Map<String, Integer> roleCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; 
                    continue;
                }

                // Split CSV line by comma and assign values
                String[] data = line.split(",");
                if (data.length < 3) {
                    System.out.println("Invalid line in CSV: " + line);
                    continue; // Skip invalid lines, error checking
                }

                String role = data[2].trim();

                // Count each role
                roleCountMap.put(role, roleCountMap.getOrDefault(role, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        // Print out the counts (for debugging)
        //System.out.println("Role counts: " + roleCountMap);
        return roleCountMap;
    }

    // Method to generate a new StaffID for a specified role
    public String generateNewStaffID(String role, Map<String, Integer> roleCountMap) {

        String prefix;
        switch (role.toLowerCase()) {
            case "doctor":
                prefix = "D";
                break;
            case "pharmacist":
                prefix = "P";
                break;
            case "administrator":
                prefix = "A";
                break;
            default:
                prefix = "U"; 
        }


        int currentCount = roleCountMap.getOrDefault(role, 0) + 1;
        
        // Update the role count in the map to reflect the new addition
        roleCountMap.put(role, currentCount);

        // Format Staff ID
        return prefix + String.format("%03d", currentCount);
    }

    public boolean checkIfUserExists(String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(userID)) {
                    return true; // User found
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return false; // User not found
    }

    public List<Staff> readStaffList() {
    List<Staff> staffList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        String line;
        br.readLine(); // Skip header line 
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            Staff staff = new Staff(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), data[5]);
            staffList.add(staff);
        }
    } catch (IOException e) {
        System.out.println("Error reading CSV file: " + e.getMessage());
    }
    return staffList;
    
    }

    public String getPatientNameByID(String patientID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length > 1 && data[0].trim().equals(patientID)) {
                    return data[1].trim(); // Assuming name is the second column
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return null; // Return null if no match is found
    }

    public String getDoctorNameByID(String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length > 1 && data[0].trim().equals(doctorID)) {
                    return data[1].trim(); 
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return null; // Return null if no match is found
    }
    

    
}
