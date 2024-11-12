package Medicine;

import Login.DisplayFormat;
import java.util.List;
import java.util.Scanner;

public class MedicineUI extends Medicine {
    MedicineData dataInterface = new MedicineData();
    
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

    public boolean updateLowStockLevelAlert(String name, int newLowStockLevel) {
    List<Medicine> medicineList = dataInterface.getAllMedicines();
    boolean updated = false;

    // Find the medicine and update its low stock level alert
        for (Medicine med : medicineList) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                dataInterface.setLowStockLevelAlert(name,newLowStockLevel); 
                updated = true;
                break;
            }
        }
        return false; // Medicine not found
    }


    public void removeMedicine(String name) {


        boolean removed = dataInterface.removeMedicineByName(name);
        
        if (removed) {
            System.out.println("Medicine successfully removed! \n");
        } else {
            System.out.println("Failed to remove medicine. Medicine may not exist in the database.\n");
        }
    }
    
    public void print(String s) {
        System.out.println(s);
    }


}
