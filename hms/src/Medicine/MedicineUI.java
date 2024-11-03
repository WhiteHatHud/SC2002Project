package Medicine;

import java.util.Scanner;

public class MedicineUI extends Medicine {
    MedicineData dataInterface = new MedicineData();
    
    public void displayAllMedicines() {
        for (Medicine med : dataInterface.getAllMedicines()) {
            print(med);
        }
        System.out.println("");
    }
    
    public void updateStock(String name, int changeAmount) {
        if (!dataInterface.updateStock(name, changeAmount)) {
            print("Failed to update stock. Medicine may not exist.\n");
        } else {
            print("Stock successfully updated.\n");
        }
    }
    
    public void addNewMedicine() {
        Scanner scanner = new Scanner(System.in);

        // Userfriendliness. Need to clean this up

        System.out.print("Enter the name of the medicine: ");
        String name = scanner.nextLine().trim();

        if (dataInterface.exists(name)) {
            System.out.println("This medicine already exists in the database.\n");
            return; // Exit the method if the medicine already exists
        }

        //Continue if Does not exist
        System.out.print("Enter the stock level (1 stock = 100mg): ");
        int stock = scanner.nextInt();

        System.out.print("Enter the low stock level alert (1 stock = 100mg): ");
        int lowStockLevel = scanner.nextInt();

        //Add the new medicine to the database
        Medicine newMedicine = new Medicine(name, stock, lowStockLevel);
        if (dataInterface.addMedicine(newMedicine)) {
            System.out.println("Medicine successfully added to database! \n");
        } else {
            System.out.println("Failed to add medicine to database.");
        }
    }
    
    public void displayOneMedicine(String name) {
        Medicine med = dataInterface.getMedicineByName(name);
        print(med);
    }
    
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
    
    public void print(String s) {
        System.out.println(s);
    }
}
