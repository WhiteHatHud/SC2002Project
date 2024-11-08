package Users;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Doctors.DoctorShared;

public class StaffData {
    private static final String FILE_PATH = "Staff_List.csv";
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
    

public boolean removeStaffByID(String staffId) {
    List<Staff> staffList = getAllStaff();
    boolean isRemoved = staffList.removeIf(n -> (n.getUserID().equals(staffId))) && updateStaffList(staffList);

    if (isRemoved) {
        System.out.println("Please inform patient to re-book his appointment");
        removeAppointmentsByStaffID(staffId);

        // Check diagnosis.csv and reassign patients if necessary
        reassignPatientsUnderDoctorCare(staffId);
    }

    return isRemoved;
}

private void removeAppointmentsByStaffID(String staffId) {
    String appointmentsFilePath = "appointments.csv";
    List<String> updatedLines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFilePath))) {
        String line;
        String header = br.readLine(); // Read and keep the header
        if (header != null) {
            updatedLines.add(header); // Add header to the new list
        }

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            // Assuming index 4 contains the staff/doctor ID in the appointments CSV
            if (data.length > 4 && !data[4].equals(staffId)) {
                updatedLines.add(line); // Keep the line if the staffId does not match
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading appointments CSV: " + e.getMessage());
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentsFilePath))) {
        for (String updatedLine : updatedLines) {
            writer.write(updatedLine);
            writer.newLine();
        }
        System.out.println("Appointments updated successfully after removing staff ID: " + staffId);
    } catch (IOException e) {
        System.out.println("Error writing to appointments CSV: " + e.getMessage());
    }
}

private void reassignPatientsUnderDoctorCare(String doctorId) {
    String diagnosisFilePath = "diagnosis.csv";
    List<String[]> diagnosisRecords = new ArrayList<>();
    List<String[]> patientsToReassign = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(diagnosisFilePath))) {
        String line;
        String header = br.readLine();
        if (header != null) {
            diagnosisRecords.add(header.split(","));
        }

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            // Assuming index 3 contains the doctor ID in the diagnosis CSV
            if (data.length > 3 && data[3].equals(doctorId)) {
                patientsToReassign.add(data); // Collect records needing reassignment
            } else {
                diagnosisRecords.add(data); // Keep existing records not needing reassignment
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading diagnosis CSV: " + e.getMessage());
        return;
    }

    // Check if there are any patients needing reassignment
    if (!patientsToReassign.isEmpty()) {
        System.out.println("The following patients need reassignment due to the removal of doctor ID " + doctorId + ":");
        for (String[] patientRecord : patientsToReassign) {
            System.out.println("Patient ID: " + patientRecord[1] + ", Patient Name: " + patientRecord[2]);
        }

        String newDoctorId;

        while (true) {
            System.out.print("Enter a valid doctor ID to reassign these patients: ");
            newDoctorId =DoctorShared.getUserInputHandler().getNextLine();
            

            // Validate if the new doctor ID exists using the checkIfUserExists method
            if (DoctorShared.getcsvUtilities2().checkIfUserExists(newDoctorId)) {
                break;
            } else {
                System.out.println("Error: The entered doctor ID does not exist. Please try again.");
            }
        }

        // Update the records with the new doctor ID
        for (String[] patientRecord : patientsToReassign) {
            patientRecord[3] = newDoctorId; // Update doctor ID in the record
            // Optionally update the doctor's name in the record
            patientRecord[4] = DoctorShared.getcsvUtilities2().getDoctorNameByID(newDoctorId);
            diagnosisRecords.add(patientRecord);
        }
    }

    // Write the updated records back to the diagnosis CSV
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(diagnosisFilePath))) {
        for (String[] record : diagnosisRecords) {
            writer.write(String.join(",", record));
            writer.newLine();
        }
        System.out.println("Diagnosis CSV updated with reassigned patients.");
    } catch (IOException e) {
        System.out.println("Error writing to diagnosis CSV: " + e.getMessage());
    }
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
