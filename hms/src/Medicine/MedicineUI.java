package Medicine;

import Login.DisplayFormat;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code MedicineUI} class provides a user interface for managing and interacting with
 * the medicine data. It includes functionalities to display all medicines, add new medicines,
 * update stock levels, remove medicines, and manage low stock alerts.
 */
public class MedicineUI extends Medicine {

    /** Interface for interacting with medicine data stored in the CSV file */
    MedicineData dataInterface = new MedicineData();

    /**
     * Displays all medicines in a formatted list with details such as medicine name,
     * initial stock, and low stock alert level.
     */
    public void displayAllMedicines() {
        DisplayFormat.clearScreen();
        System.out.printf("%-25s %-15s %-20s%n", "Medicine Name", "Initial Stock (1=100mg)", "Low Stock Level Alert");
        System.out.println("---------------------------------------------------------------");
        int i = 1;

        // Print each medicine's details with formatting
        for (Medicine med : dataInterface.getAllMedicines()) {
            System.out.printf((i++) + ". %-25s %-15d %-20d%n", 
                              med.getMedicineName(), 
                              med.getInitialStock(), 
                              med.getLowStockLevelAlert());
        }
        System.out.println(); 
    }

    /**
     * Updates the stock level of a specific medicine.
     *
     * @param name the name of the medicine to update
     * @param changeAmount the amount by which to change the stock level (positive to increase, negative to decrease)
     */
    public void updateStock(String name, int changeAmount) {
        if (!dataInterface.updateStock(name, changeAmount)) {
            print("Failed to update stock. Medicine may not exist.\n");
        } else {
            print("Stock successfully updated.\n");
        }
    }

    /**
     * Adds a new medicine to the database. Prompts the user for details such as
     * medicine name, stock level, and low stock alert level.
     */
    public void addNewMedicine() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the medicine: ");
        String name = scanner.nextLine().trim();

        if (dataInterface.exists(name)) {
            System.out.println("This medicine already exists in the database.\n");
            return; // Exit the method if the medicine already exists
        }

        // Continue if the medicine does not exist
        System.out.print("Enter the stock level (1 stock = 100mg): ");
        int stock = scanner.nextInt();

        System.out.print("Enter the low stock level alert (1 stock = 100mg): ");
        int lowStockLevel = scanner.nextInt();

        // Add the new medicine to the database
        Medicine newMedicine = new Medicine(name, stock, lowStockLevel);
        if (dataInterface.addMedicine(newMedicine)) {
            System.out.println("Medicine successfully added to database! \n");
        } else {
            System.out.println("Failed to add medicine to database.");
        }
    }

    /**
     * Displays information about a specific medicine.
     *
     * @param name the name of the medicine to display
     */
    public void displayOneMedicine(String name) {
        Medicine med = dataInterface.getMedicineByName(name);
        print(med);
    }

    /**
     * Prints the details of a medicine object.
     *
     * @param med the {@code Medicine} object to print
     */
    public void print(Medicine med) {
        if (med == null) {
            print("Medicine not found\n");
            return;
        }
        System.out.println(
            med.getMedicineName() + ", " + 
            med.getInitialStock() + ", " +
            med.getLowStockLevelAlert()
        );
    }

    /**
     * Updates the low stock level alert for a specific medicine.
     *
     * @param name the name of the medicine to update
     * @param newLowStockLevel the new low stock level alert to set
     * @return {@code true} if the low stock alert level was successfully updated, {@code false} if the medicine was not found
     */
    public boolean updateLowStockLevelAlert(String name, int newLowStockLevel) {
        List<Medicine> medicineList = dataInterface.getAllMedicines();
        boolean updated = false;

        // Find the medicine and update its low stock level alert
        for (Medicine med : medicineList) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                dataInterface.setLowStockLevelAlert(name, newLowStockLevel); 
                updated = true;
                break;
            }
        }
        return updated;
    }

    /**
     * Removes a medicine from the database by its name.
     *
     * @param name the name of the medicine to remove
     */
    public void removeMedicine(String name) {
        boolean removed = dataInterface.removeMedicineByName(name);
        
        if (removed) {
            System.out.println("Medicine successfully removed! \n");
        } else {
            System.out.println("Failed to remove medicine. Medicine may not exist in the database.\n");
        }
    }

    /**
     * Prints a message to the console.
     *
     * @param s the message to print
     */
    public void print(String s) {
        System.out.println(s);
    }
}
