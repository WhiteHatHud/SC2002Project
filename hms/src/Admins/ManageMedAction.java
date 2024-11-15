package Admins;

import Login.DisplayManager;
import Medicine.Medicine;
import Medicine.MedicineData;
import Medicine.MedicineUI;
import Utilities.UserInputHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The ManageMedAction class handles medication inventory management actions within the admin context.
 * This class provides options for viewing, adding, updating, and removing medications, as well as approving
 * replenishment requests from pharmacists.
 */
public class ManageMedAction {

    private MedicineUI med = new MedicineUI();
    private MedicineData data = new MedicineData();
    private UserInputHandler input = new UserInputHandler();

    /**
     * Starts the medication management menu, allowing the admin to choose from various options
     * related to medication inventory.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            DisplayManager.clearScreen();
            AdminShared.getDisplayManager().manageMedicationInvetoryMenu();

            System.out.print("Please enter your choice: ");
            choice = input.getPositiveInt();

            switch (choice) {
                case 1 -> viewInventory();
                case 2 -> addMedication();
                case 3 -> updateStockLevel();
                case 4 -> removeMedication();
                case 5 -> updateLowStockAlert();
                case 6 -> approveReplenishmentRequests();
                case 7 -> {
                    System.out.println("Returning to the main menu...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (choice != 7) {
                System.out.println("Press Enter to return to the Manage Medication Inventory menu...");
                scanner.nextLine();
            }

        } while (true);
    }

    /**
     * Displays the current inventory of medications.
     */
    private void viewInventory() {
        DisplayManager.clearScreen();
        System.out.println("Displaying current medication inventory...\n");
        med.displayAllMedicines();
    }

    /**
     * Initiates the process for adding a new medication to the inventory.
     */
    private void addMedication() {
        DisplayManager.clearScreen();
        System.out.println("Adding a new type of medicine...\n");
        med.addNewMedicine();
    }

    /**
     * Updates the stock level of an existing medication in the inventory.
     */
    private void updateStockLevel() {
        DisplayManager.clearScreen();
        System.out.println("Updating stock level...\n");

        List<Medicine> medicines = data.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines found in the inventory.");
            return;
        }

        System.out.println("Select a medicine to update:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            System.out.printf("%d. %s (Current stock: %d)\n", i + 1, med.getMedicineName(), med.getInitialStock());
        }

        System.out.print("Enter the number of the medicine you want to update: ");
        int choice = input.getPositiveInt();

        if (choice < 1 || choice > medicines.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Medicine selectedMedicine = medicines.get(choice - 1);
        System.out.print("Enter the amount to change the stock level (positive to increase, negative to decrease): ");
        int changeAmount = input.getNonZero();

        int newStockLevel = selectedMedicine.getInitialStock() + changeAmount;
        if (newStockLevel < 0) {
            System.out.println("Invalid operation: resulting stock level cannot be negative.");
            return;
        }

        med.updateStock(selectedMedicine.getMedicineName(), changeAmount);
        System.out.println("Stock level updated successfully.");
    }

    /**
     * Removes a medication from the inventory.
     */
    private void removeMedication() {
        DisplayManager.clearScreen();
        System.out.println("Removing Medication...\n");

        List<Medicine> medicines = data.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines found in the inventory.");
            return;
        }

        System.out.println("Select a medicine to remove:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            System.out.printf("%d. %s (Current stock: %d)\n", i + 1, med.getMedicineName(), med.getInitialStock());
        }

        System.out.print("Enter the number of the medicine you want to remove: ");
        int choice = input.getPositiveInt();

        if (choice < 1 || choice > medicines.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Medicine selectedMedicine = medicines.get(choice - 1);
        System.out.print("Are you sure you want to remove " + selectedMedicine.getMedicineName() + " from the inventory? (yes/no): ");
        String confirmation = input.getNextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Operation cancelled. Medicine was not removed.");
            return;
        }

        med.removeMedicine(selectedMedicine.getMedicineName());
        System.out.println("Medicine " + selectedMedicine.getMedicineName() + " has been successfully removed from the inventory.");
    }

    /**
     * Updates the low stock alert level for a specified medication.
     */
    private void updateLowStockAlert() {
        DisplayManager.clearScreen();
        System.out.println("Updating Low Stock Alert Level...\n");

        List<Medicine> medicines = data.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines found in the inventory.");
            return;
        }

        System.out.println("Select a medicine to update its low stock alert level:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            System.out.printf("%d. %s (Current low stock alert: %d, Current stock: %d)\n", 
                              i + 1, med.getMedicineName(), med.getLowStockLevelAlert(), med.getInitialStock());
        }

        System.out.print("Enter the number of the medicine you want to update: ");
        int choice = input.getPositiveInt();

        Medicine selectedMedicine = medicines.get(choice - 1);
        System.out.print("Enter the new low stock alert level: ");
        int newLowStockLevel = input.getPositiveInt();

        med.updateLowStockLevelAlert(selectedMedicine.getMedicineName(), newLowStockLevel);
    }

public void approveReplenishmentRequests() {
    String requestFilePath = "././RequestFromPharma.csv";
    String inventoryFilePath = "././Medicine_List.csv";
    List<String[]> requests = new ArrayList<>();

    // Read the requests from the request CSV
    try (BufferedReader reader = new BufferedReader(new FileReader(requestFilePath))) {
        String line;
        reader.readLine(); // Skip header row
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            requests.add(data);
        }
    } catch (IOException e) {
        System.out.println("Error reading replenishment requests.");
        e.printStackTrace();
        return;
    }

    // Display requests as options for approval
    if (requests.isEmpty()) {
        System.out.println("No replenishment requests available.");
        return;
    }

    System.out.println("=== Replenishment Requests ===");
    System.out.printf("%-5s %-20s %-10s %-15s %-15s%n", "ID", "Medicine Name", "Quantity", "PharmaID", "PharmaName");
    System.out.println("---------------------------------------------------------------------");
    for (int i = 0; i < requests.size(); i++) {
        String[] request = requests.get(i);
        System.out.printf("%-5s %-20s %-10s %-15s %-15s%n", request[0], request[1], request[2], request[3], request[4]);
    }

    // Prompt the admin to select a request to approve
    System.out.print("Enter the ID of the request you want to approve: ");
    int choice = input.getPositiveInt() - 1;

    if (choice < 0 || choice >= requests.size()) {
        System.out.println("Invalid choice.");
        return;
    }

    // Process the selected request
    String[] approvedRequest = requests.get(choice);
    String medicineName = approvedRequest[1];
    int quantityToAdd = Integer.parseInt(approvedRequest[2]);

    // Confirm approval
    System.out.printf("Are you sure you want to approve the request to add %d units of %s? (yes/no): ", quantityToAdd, medicineName);
    String confirmation = input.getNextLine().trim().toLowerCase();
    if (!confirmation.equals("yes")) {
        System.out.println("Approval canceled.");
        return;
    }

    // Update the medicine inventory in the Medicine_List.csv
    List<String[]> inventory = new ArrayList<>();
    boolean medicineFound = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFilePath))) {
        inventory.add(reader.readLine().split(",")); // Add header row
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equalsIgnoreCase(medicineName)) {
                int currentStock = Integer.parseInt(data[1].trim());
                data[1] = String.valueOf(currentStock + quantityToAdd); // Update stock level
                medicineFound = true;
            }
            inventory.add(data);
        }
    } catch (IOException e) {
        System.out.println("Error reading medicine inventory.");
        e.printStackTrace();
        return;
    }

    // Check if the medicine was found and update inventory file
    if (!medicineFound) {
        System.out.println("Medicine not found in inventory. Approval cannot proceed.");
        return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFilePath))) {
        for (String[] data : inventory) {
            writer.write(String.join(",", data));
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Failed to update inventory.");
        e.printStackTrace();
        return;
    }

    // Remove the approved request and re-ID the remaining requests
    requests.remove(choice);

    // Update IDs to be sequential, starting from 1
    for (int i = 0; i < requests.size(); i++) {
        requests.get(i)[0] = String.valueOf(i + 1);
    }

    // Write the updated requests back to the request CSV
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(requestFilePath))) {
        writer.write("ID,Medicine Name,Quantity,PharmaID,PharmaName");
        writer.newLine();
        for (String[] request : requests) {
            writer.write(String.join(",", request));
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Failed to update replenishment requests.");
        e.printStackTrace();
        return;
    }

    System.out.println("Request approved and inventory updated successfully for " + medicineName + ".\n");
}
}
