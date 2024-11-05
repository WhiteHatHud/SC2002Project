package Pharmacists;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UpdateInventory {
    private final String medicineCsvFilePath = "Medicine_List.csv";


    static class Medicine {
        int stock;
        int lowStockAlert;

        Medicine(int stock, int lowStockAlert) {
            this.stock = stock;
            this.lowStockAlert = lowStockAlert;
        }
    }
    int getAvailableStockInMg(String medicineName) {
        Map<String, Medicine> medicineStock = loadMedicineStock();
    
        // Check if the medicine exists in inventory
        if (!medicineStock.containsKey(medicineName)) {
            System.out.println("Medicine " + medicineName + " not found in inventory.");
            return 0; // Return 0 mg if the medicine is not found
        }
    
        // 1 stock unit = 100mg, so total stock *= 100
        Medicine medicine = medicineStock.get(medicineName);
        return medicine.stock * 100;
    }
    

    boolean decrementStock(String medicineName, int dosageInMg) {
        Map<String, Medicine> medicineStock = loadMedicineStock();
    
        // Check if the medicine exists in inventory
        if (!medicineStock.containsKey(medicineName)) {
            System.out.println("Medicine " + medicineName + " not found in inventory.");
            return false;
        }
    
        Medicine medicine = medicineStock.get(medicineName);
        int availableStockInMg = medicine.stock * 100; // Convert stock units to mg
    
        // Check if there is enough stock
        if (availableStockInMg < dosageInMg) {
            return false; // Return false without printing
        }
    
        // Calculate new stock in mg after dispensing the required dosage
        int remainingStockInMg = availableStockInMg - dosageInMg;
        medicine.stock = remainingStockInMg / 100; // Convert back to stock units
    
        // Warn if stock is below the low stock alert level after dispensing
        if (medicine.stock < medicine.lowStockAlert) {
            System.out.println("Warning: " + medicineName + " stock is below the low stock alert level (" + medicine.lowStockAlert + " units).");
        }
    
        // Save the updated stock
        return saveMedicineStock(medicineStock);
    }

    public boolean isBelowLowStockAlert(String medicineName) {
    Map<String, Medicine> medicineStock = loadMedicineStock();
    Medicine medicine = medicineStock.get(medicineName);

    if (medicine != null) {
        return medicine.stock < medicine.lowStockAlert;
    }
    return false;
}


    // Load the stock levels and low stock alerts from the Medicine CSV
    Map<String, Medicine> loadMedicineStock() {
        Map<String, Medicine> medicineStock = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(medicineCsvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }
                String[] rowData = line.split(",");
                String medicineName = rowData[0].trim();
                int stock = Integer.parseInt(rowData[1].trim());
                int lowStockAlert = Integer.parseInt(rowData[2].trim());
                medicineStock.put(medicineName, new Medicine(stock, lowStockAlert));
            }
        } catch (IOException e) {
            System.out.println("Error reading Medicine CSV file: " + e.getMessage());
        }
        return medicineStock;
    }

    // Save the updated stock levels back to the Medicine CSV
    boolean saveMedicineStock(Map<String, Medicine> medicineStock) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(medicineCsvFilePath))) {

            pw.println("Medicine Name,Initial Stock,Low Stock Level Alert");

            // Write each medicine's stock level and low stock alert level back to the CSV
            for (Map.Entry<String, Medicine> entry : medicineStock.entrySet()) {
                String medicineName = entry.getKey();
                Medicine medicine = entry.getValue();
                pw.println(medicineName + "," + medicine.stock + "," + medicine.lowStockAlert);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to Medicine CSV file: " + e.getMessage());
            return false;
        }
    }
}
