package Pharmacists;

import Login.DisplayFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UpdatePrescriptionStatus {
    private final String csvFilePath = "To be prescribed.csv";

    public boolean updateStatus(String searchField, String searchValue) {
        List<String[]> csvData = new ArrayList<>();
        boolean updated = false;
        boolean foundPrescription = false; // Flag to check if the prescription exists
        UpdateInventory updateInventory = new UpdateInventory();
        Scanner scanner = new Scanner(System.in); // Scanner to get user input for confirmation
        
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
                    csvData.add(rowData);
                    isFirstLine = false;
                    continue;
                }

                // Check if this is the row to update based on search criteria
                if (rowData.length > 7 && rowData[searchIndex].trim().equalsIgnoreCase(searchValue)) {
                    foundPrescription = true;

                    // Skip further processing if the prescription is already dispensed
                    if ("Dispensed".equalsIgnoreCase(rowData[7].trim())) {
                        System.out.println("Medicine already dispensed!");
                        return false; // Stop processing since it's already dispensed
                    }

                    // Display prescription details
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

                    // Ask for confirmation to dispense
                    System.out.print("Do you want to dispense this prescription? (yes/no): ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    if (!confirmation.equals("yes")) {
                        System.out.println("Dispense operation canceled.");
                        return false;
                    }

                    // Parse medications to check stock and dispense
                    String medications = rowData[6].trim();
                    String[] medicationPairs = medications.split(";");
                    boolean sufficientStock = true;
                    StringBuilder insufficientStockMessage = new StringBuilder("Unable to dispense medications due to insufficient stock for:\n");
                    StringBuilder dispensedMessage = new StringBuilder("Medicine Dispensed:\n");

                    for (String pair : medicationPairs) {
                        String[] parts = pair.trim().split(":");
                        if (parts.length != 2) {
                            System.out.println("Invalid medication format for: " + pair);
                            sufficientStock = false;
                            break;
                        }

                        String medicineName = parts[0].trim();
                        int requiredDosageInMg;
                        try {
                            requiredDosageInMg = Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid dosage format for " + medicineName + ": " + parts[1].trim());
                            sufficientStock = false;
                            break;
                        }

                        // Check stock and decrement if sufficient
                        if (!updateInventory.decrementStock(medicineName, requiredDosageInMg)) {
                            insufficientStockMessage.append("- ").append(medicineName)
                                .append(" (Required: ").append(requiredDosageInMg)
                                .append("mg, Available: ")
                                .append(updateInventory.getAvailableStockInMg(medicineName)).append("mg)\n");
                            sufficientStock = false;
                        } else {
                            dispensedMessage.append("- ").append(medicineName)
                                .append(": ").append(requiredDosageInMg).append("mg\n");
                        }
                    }

                    // Update status if stock is sufficient
                    if (sufficientStock) {
                        rowData[7] = "Dispensed"; 
                        updated = true;
                        System.out.println("Prescription status updated to Dispensed.");
                        System.out.println(dispensedMessage.toString());
                    } else {
                        System.out.println(insufficientStockMessage.toString());
                    }
                }

                csvData.add(rowData);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return false;
        }
        
        // Write the updated data back to the CSV file if an update was made
        if (updated) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(csvFilePath))) {
                for (String[] row : csvData) {
                    pw.println(String.join(",", row));
                }
            } catch (IOException e) {
                System.out.println("Error writing to CSV file: " + e.getMessage());
                return false;
            }
        } else if (foundPrescription && !updated) {
            System.out.println("Prescription could not be dispensed due to insufficient stock for one or more medications.");
        } else if (!foundPrescription) {
            System.out.println("No matching prescription found for " + searchField + ": " + searchValue);
        }
        
        return updated;
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
            case 1:
                searchField = "PrescriptionID";
                break;
            case 2:
                searchField = "PatientID";
                break;
            case 3:
                searchField = "PatientName";
                break;
            default:
                System.out.println("Invalid choice. Returning to menu.");
                return;
        }

        System.out.print("Enter " + searchField + ": ");
        String searchValue = scanner.nextLine().trim();
        DisplayFormat.clearScreen();

        updateStatus(searchField, searchValue);
    }
}
