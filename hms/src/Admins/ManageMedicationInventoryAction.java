package Admins;
import Login.DisplayManager;
import Medicine.MedicineUI;

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
                    //addMedication();
                    break;
                case 3:
                    //updateStockLevel();
                    break;
                case 4:
                    //removeMedication();
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

@Override
public String getDescription() {
    return "Manage Inventory";
}


}