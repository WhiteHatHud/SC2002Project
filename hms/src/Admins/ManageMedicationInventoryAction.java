package Admins;
import Login.DisplayManager;
import Medicine.MedicineUI;
import java.util.Scanner;

public class ManageMedicationInventoryAction implements MenuAction{

    MedicineUI med = new MedicineUI();

    public void execute() {
        DisplayManager.clearScreen();
        int choice;

        do {
            System.out.println("=== Medication Inventory Management ===");
            System.out.println("1. View Medication Inventory");
            System.out.println("2. Add New Medication");
            System.out.println("3. Update Medication Stock Level");
            System.out.println("4. Remove Medication");
            System.out.println("5. Update Low Stock Alert Level");
            System.out.println("6. Approve Replenishment Requests");
            System.out.println("7. Exit");

            System.out.print("Please enter your choice: ");
            choice = AdminShared.getUserInputHandler().getUserChoice();


            switch (choice) {
                case 1:
                    viewInventory();
                    break;
                case 2:
                    addMedication();
                    break;
                case 3:
                    updateStockLevel();
                    break;
                case 4:
                    removeMedication();
                    break;
                case 5:
                    //updateLowStockAlert();
                    break;
                case 6:
                    //approveReplenishmentRequests();
                    break;
                case 7:
                    System.out.println("Exiting Medication Inventory Management.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
        

    }


private void viewInventory() {
    DisplayManager.clearScreen();
    System.out.println("Displaying current medication inventory...\n");
    med.displayAllMedicines();
}

private void addMedication() {
    DisplayManager.clearScreen();
    System.out.println("Adding a new type of medicine...\n");
    med.addNewMedicine();
}

private void updateStockLevel() {
    DisplayManager.clearScreen();
    System.out.println("Updating stock level...\n");

    Scanner scanner = new Scanner(System.in);

    // Ask the user for the name of the medicine
    System.out.print("Enter the name of the medicine: ");
    String name = scanner.nextLine().trim();

    // Ask the user for the change amount
    System.out.print("Enter the amount to change the stock level (positive to increase, negative to decrease): ");
    int changeAmount = scanner.nextInt();
    med.updateStock(name, changeAmount);
}

private void removeMedication() {
    DisplayManager.clearScreen();
    System.out.println("Removing Medication...\n");

    Scanner scanner = new Scanner(System.in);

    // Ask the user for the name of the medicine
    System.out.print("Enter the name of the medicine: ");
    String name = scanner.nextLine().trim();
    med.removeMedicine(name);
}
@Override
public String getDescription() {
    return "Manage Inventory";
}



}