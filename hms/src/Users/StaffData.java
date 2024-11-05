package Users;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffData {
    private static final String FILE_PATH = "././Staff_List.csv";
    Staff temp;
    public StaffData(){}

    public List<Staff> getAllStaff(){
        List<Staff> staffList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine();
            // Skip header row
            while ((line = reader.readLine()) != null) {
                // System.out.println("Reading line: " + line); // Debugging print statement
                String[] data = line.split(",");
                if (data.length < 7) {
                    //System.out.println("Skipping incomplete line: " + line); // Debugging print for skipped lines
                    continue;
                }
                try {
                    int age = Integer.parseInt(data[4].trim());
                    Staff stf = new Staff(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), age, data[5].trim(),data[6].trim());
                    staffList.add(stf);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    public Staff getStaffByID(String ID){
        List<Staff> staffList = getAllStaff();
        for (Staff staff : staffList){
            if(staff.getUserID().equals(ID)){
                return staff;
            }
        }
        return null;
    }
    public boolean addStaff(Staff staff){
        if (exists(staff.getUserID())) {
            return false;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(staff.toCSV() + ",password"); 
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public boolean removeStaffByID(String staffId){
        List<Staff> staffList = getAllStaff();
        return staffList.removeIf(n -> (n.getUserID().equals(staffId))) && updateStaffList(staffList);
    }
 
    public boolean updateStaffList(List<Staff> staffList){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Staff ID,Name,Role,Gender,Age,Office Number,Password");
            writer.newLine();
            for (Staff staff : staffList) {
                writer.write(staff.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String ID){
        if (getStaffByID(ID) != null){
            return true;
        }
        return false;
    }

        public Map<String, Integer> countRolesAndGenerateID() {
        Map<String, Integer> roleCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 3) {
                    System.out.println("Invalid line in CSV: " + line);
                    continue;
                }

                String role = data[2].trim();
                roleCountMap.put(role, roleCountMap.getOrDefault(role, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        return roleCountMap;
    }

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

        int highestID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].startsWith(prefix)) {
                    int idNumber = Integer.parseInt(fields[0].substring(1));
                    if (idNumber > highestID) {
                        highestID = idNumber;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int newIDNumber = highestID + 1;
        roleCountMap.put(role, newIDNumber);
        return prefix + String.format("%03d", newIDNumber);
    }


}
