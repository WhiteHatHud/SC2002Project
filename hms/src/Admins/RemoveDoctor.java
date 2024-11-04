package Admins;

import Login.DisplayManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveDoctor {
    private String doctorCsvFilePath;
    private String appointmentCsvFilePath;

    public RemoveDoctor(String doctorCsvFilePath, String appointmentCsvFilePath) {
        this.doctorCsvFilePath = doctorCsvFilePath;
        this.appointmentCsvFilePath = appointmentCsvFilePath;
    }

    public void start() {
        DisplayManager.clearScreen();

        System.out.print("Enter Doctor ID to remove: ");
        String doctorID = AdminShared.getUserInputHandler().getNextLine();

        // Check if the Doctor ID exists in the doctor CSV file and display their information
        String[] doctorInfo = getDoctorInfoByID(doctorID);
        if (doctorInfo == null) {
            System.out.println("Doctor ID not found.");
            return;
        }

        // Display doctor information
        System.out.println("Doctor information:");
        System.out.println("ID: " + doctorInfo[0]);
        System.out.println("Name: " + doctorInfo[1]);
        System.out.println("Role: " + doctorInfo[2]);
        System.out.println("Gender: " + doctorInfo[3]);
        System.out.println("Age: " + doctorInfo[4]);
        System.out.println("Office Number: " + doctorInfo[5]);
        System.out.println("Password: " + doctorInfo[6]);

        // Confirm deletion
        System.out.print("Are you sure you want to delete Doctor ID " + doctorID + "? (yes/no): ");
        String confirmation = AdminShared.getUserInputHandler().getNextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }

        // Proceed to remove the doctor from both files
        removeDoctorByID(doctorID);
        removeDoctorAppointments(doctorID);

        DisplayManager.clearScreen();
        System.out.println("Doctor with ID " + doctorID + " has been successfully removed from all records.");
    }

    private String[] getDoctorInfoByID(String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader(doctorCsvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }

                // If the row matches the doctor ID, return the doctor's data
                if (data[0].trim().equals(doctorID)) {
                    return data;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading doctor CSV file: " + e.getMessage());
        }
        return null; // Doctor not found
    }

    private void removeDoctorByID(String doctorID) {
        List<String[]> csvData = new ArrayList<>();
        String[] headers = null;

        // Read all data except the specified row
        try (BufferedReader br = new BufferedReader(new FileReader(doctorCsvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    headers = data;
                    isHeader = false;
                    csvData.add(headers); // Keep the headers
                    continue;
                }

                // Skip the row if it matches the ID to be removed
                if (data[0].trim().equals(doctorID)) {
                    continue;
                }

                csvData.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error reading doctor CSV file: " + e.getMessage());
            return;
        }

        // Write the updated data back to the doctor CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(doctorCsvFilePath))) {
            for (String[] row : csvData) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.out.println("Error writing to doctor CSV file: " + e.getMessage());
        }
    }

    private void removeDoctorAppointments(String doctorID) {
        List<String[]> csvData = new ArrayList<>();
        String[] headers = null;
    
        // Read all data except the rows with the specified doctor ID in the 5th column (index 4)
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentCsvFilePath))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
    
                if (isHeader) {
                    headers = data;
                    isHeader = false;
                    csvData.add(headers); // Keep the headers
                    continue;
                }
    
                // Skip entire row if it matches the doctor ID in the 5th column (index 4)
                if (data[4].trim().equals(doctorID)) {
                    continue;
                }
    
                csvData.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error reading appointment CSV file: " + e.getMessage());
            return;
        }
    
        // Write the updated data back to the appointment CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(appointmentCsvFilePath))) {
            for (String[] row : csvData) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.out.println("Error writing to appointment CSV file: " + e.getMessage());
        }
    }
    
}
