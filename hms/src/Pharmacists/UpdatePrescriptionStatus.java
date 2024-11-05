package Pharmacists;

import Login.DisplayFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UpdatePrescriptionStatus {
    private final String csvFilePath = "To be prescribed.csv";
    private Pharmacist pharma;

    public UpdatePrescriptionStatus(Pharmacist pharma){
        this.pharma=pharma;
    }
    public boolean updateStatus(String searchField, String searchValue) {
        List<String[]> csvData = new ArrayList<>();
        boolean updated = false;
        boolean foundPrescription = false;
        UpdateInventory updateInventory = new UpdateInventory();
        Scanner scanner = new Scanner(System.in);


        int searchIndex = switch (searchField.toLowerCase()) {
            case "prescriptionid" -> 0;
            case "patientid" -> 1;
            case "patientname" -> 2;
            default -> -1;
        };

        if (searchIndex == -1) {
            System.out.println("Invalid search field.");
            return false;
        }


        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(",");
                if (isFirstLine) {
                    csvData.add(rowData); // add header row
                    isFirstLine = false;
                    continue;
                }

                // Check if this row matches the search criteria
                if (rowData.length > 7 && rowData[searchIndex].trim().equalsIgnoreCase(searchValue)) {
                    foundPrescription = true;

                    if ("Dispensed".equalsIgnoreCase(rowData[7].trim())) {
                        System.out.println("This prescription has already been dispensed.");
                        return false; // Stop further processing
                    }

                    displayPrescriptionDetails(rowData);

                    // Ask for confirmation to dispense
                    System.out.print("Do you want to dispense this prescription? (yes/no): ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    if (!"yes".equals(confirmation)) {
                        System.out.println("Dispense operation canceled.");
                        return false;
                    }

                    // Process medications for stock check and dispensing
                    if (processMedications(rowData[6], updateInventory)) {
                        rowData[7] = "Dispensed"; // Update status to dispensed
                        updated = true;
                    } else {
                        System.out.println("Prescription could not be dispensed due to insufficient stock.");
                    }
                }

                csvData.add(rowData); 
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return false;
        }


        if (updated) {
            writeDataToCSV(csvData);
        } else if (!foundPrescription) {
            System.out.println("No matching prescription found for " + searchField + ": " + searchValue);
        }

        return updated;
    }

    // Helper method to display prescription details
    private void displayPrescriptionDetails(String[] rowData) {
        System.out.println("==== Prescription Details ====");
        System.out.println("Prescription ID    : " + rowData[0].trim());
        System.out.println("Patient ID         : " + rowData[1].trim());
        System.out.println("Patient Name       : " + rowData[2].trim());
        System.out.println("Doctor ID          : " + rowData[3].trim());
        System.out.println("Doctor Name        : " + rowData[4].trim());
        System.out.println("Date Prescribed    : " + rowData[5].trim());
        System.out.println("Medications (mg)   : " + rowData[6].trim());
        System.out.println("Status             : " + rowData[7].trim());
        System.out.println("Notes              : " + rowData[8].trim());
    }

private boolean processMedications(String medications, UpdateInventory updateInventory) {
    String[] medicationPairs = medications.split(";");
    boolean sufficientStock = true;
    StringBuilder insufficientStockMessage = new StringBuilder("Unable to dispense medications due to insufficient stock for:\n");
    StringBuilder dispensedMessage = new StringBuilder("Medicine Dispensed:\n");
    SubmitRequest submitRequest = new SubmitRequest(); 
    Scanner scanner = new Scanner(System.in);

    for (String pair : medicationPairs) {
        pair = pair.trim();
        
        // Check format validity
        if (pair.isEmpty() || !pair.contains(":") || pair.indexOf(":") != pair.lastIndexOf(":")) {
            System.out.println("Invalid format for medication entry: " + pair + ". Expected format is 'MedicineName:Dosage'. Skipping this entry.");
            sufficientStock = false;
            continue;
        }

        String[] parts = pair.split(":");
        if (parts.length != 2) {
            System.out.println("Invalid medication format for: " + pair);
            sufficientStock = false;
            continue;
        }

        String medicineName = parts[0].trim();
        int requiredDosageInMg;
        try {
            requiredDosageInMg = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid dosage format for " + medicineName + ": " + parts[1].trim() + ". Expected a numeric value. Skipping this entry.");
            sufficientStock = false;
            continue;
        }

        // Check stock and decrement if sufficient
        if (!updateInventory.decrementStock(medicineName, requiredDosageInMg)) {
            insufficientStockMessage.append("- ").append(medicineName)
                .append(" (Required: ").append(requiredDosageInMg)
                .append("mg, Available: ")
                .append(updateInventory.getAvailableStockInMg(medicineName)).append("mg)\n");
            sufficientStock = false;
        } else {
            int remainingStockInMg = updateInventory.getAvailableStockInMg(medicineName); // Get remaining stock
            dispensedMessage.append("- ").append(medicineName)
                .append(": ").append(requiredDosageInMg).append("mg dispensed, Remaining stock: ")
                .append(remainingStockInMg).append("mg\n");

            // Check if the remaining stock is below the low stock alert level
            if (updateInventory.isBelowLowStockAlert(medicineName)) {  
                System.out.println("Warning: " + medicineName + " stock is below the low stock alert level.");
                System.out.print("Would you like to request a restock? (yes/no): ");
                String requestConfirmation = scanner.nextLine().trim().toLowerCase();

                if ("yes".equals(requestConfirmation)) {
                    // Use SubmitRequest to send a restock request
                    submitRequest.request(pharma.getUserID(), pharma.getName()); 
                }
            }
        }
    }

    // Display messages based on stock availability
    if (sufficientStock) {
        System.out.println(dispensedMessage.toString());
    } else {
        System.out.println(insufficientStockMessage.toString());
    }

    return sufficientStock;
}



    // Helper method to write updated data back to CSV
    private void writeDataToCSV(List<String[]> csvData) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFilePath))) {
            for (String[] row : csvData) {
                pw.println(String.join(",", row));
            }
            System.out.println("Medicine Dispensed Success! ");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void promptUpdateStatus() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("I want to dispense medicine according to: ");
        System.out.println("1. Prescription ID");
        System.out.println("2. Patient ID");
        System.out.println("3. Patient Name");
        System.out.print("Enter your choice (1, 2, or 3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();
        String searchField;
        switch (choice) {
            case 1 -> searchField = "prescriptionid";
            case 2 -> searchField = "patientid";
            case 3 -> searchField = "patientname";
            default -> {
                System.out.println("Invalid choice. Returning to menu.");
                return;
            }
        }

        System.out.print("Enter " + searchField + ": ");
        String searchValue = scanner.nextLine().trim();
        DisplayFormat.clearScreen();

        updateStatus(searchField, searchValue);
    }
}
