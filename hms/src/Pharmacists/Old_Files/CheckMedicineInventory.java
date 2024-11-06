package Pharmacists.Old_Files;

import java.util.Map;

import Pharmacists.Old_Files.UpdateInventory.Medicine;

public class CheckMedicineInventory {
    private final String medicineCsvFilePath = "Medicine_List.csv";
    
    // Method to load and display the current inventory details
    public void displayInventory() {
        UpdateInventory updateInventory = new UpdateInventory();
        Map<String, UpdateInventory.Medicine> medicineStock = updateInventory.loadMedicineStock();

        System.out.println("=========== Medicine Inventory =========== ");
        System.out.printf("%-20s %-15s %-20s\n", "Medicine Name", "Stock (units)", "Low Stock Alert");

        for (Map.Entry<String, UpdateInventory.Medicine> entry : medicineStock.entrySet()) {
            String medicineName = entry.getKey();
            UpdateInventory.Medicine medicine = entry.getValue();
            System.out.printf("%-20s %-15d %-20d\n", medicineName, medicine.stock, medicine.lowStockAlert);
        }
        
        System.out.println("---------------------------------------------------");
        System.out.println("Note: Each unit represents 100 mg.");
    }
}
