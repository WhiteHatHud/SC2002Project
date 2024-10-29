package Users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                if (data.length < 5) {
                    //System.out.println("Skipping incomplete line: " + line); // Debugging print for skipped lines
                    continue;
                }
                try {
                    int age = Integer.parseInt(data[4].trim());
                    Staff stf = new Staff(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), age);
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
    public boolean exists(String ID){
        if (getStaffByID(ID) != null){
            return true;
        }
        return false;
    }

}
