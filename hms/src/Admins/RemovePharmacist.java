package Admins;

import Login.DisplayManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RemovePharmacist {
    private String csvFilePath;

    public RemovePharmacist(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public void start() {
        DisplayManager.clearScreen();

        System.out.print("Enter Pharmacist ID to remove: ");
        String pharmaID = AdminShared.getUserInputHandler().getNextLine();

        // Check if the Pharmacist ID exists in the CSV file and display their information
        String[] pharmacistInfo = getPharmacistInfoByID(pharmaID);
        if (pharmacistInfo == null) {
            System.out.println("Pharmacist ID not found.");
            return;
        }

        // Display pharmacist information
        System.out.println("Pharmacist information:");
        System.out.println("ID: " + pharmacistInfo[0]);
        System.out.println("Name: " + pharmacistInfo[1]);
        System.out.println("Role: " + pharmacistInfo[2]);
        System.out.println("Gender: " + pharmacistInfo[3]);
        System.out.println("Age: " + pharmacistInfo[4]);
        System.out.println("Office Number: " + pharmacistInfo[5]);
        System.out.println("Password: " + pharmacistInfo[6]);

        // Confirm deletion
        System.out.print("Are you sure you want to delete Pharmacist ID " + pharmaID + "? (yes/no): ");
        String confirmation = AdminShared.getUserInputHandler().getNextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }

        // Proceed to remove the pharmacist
        removePharmacistByID(pharmaID);
        DisplayManager.clearScreen();
        System.out.println("Pharmacist with ID " + pharmaID + " has been successfully removed.");
    }

    private String[] getPharmacistInfoByID(String pharmaID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }

                // If the row matches the pharmacist ID, return the pharmacist's data
                if (data[0].trim().equals(pharmaID)) {
                    return data;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return null; // Pharmacist not found
    }

    private void removePharmacistByID(String pharmaID) {
        List<String[]> csvData = new ArrayList<>();
        String[] headers = null;

        // Read all data except the specified row
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
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
                if (data[0].trim().equals(pharmaID)) {
                    continue;
                }

                csvData.add(data); 
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFilePath))) {
            for (String[] row : csvData) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
