package Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineData {
    private static final String FILE_PATH = "././Medicine_List.csv";

    public MedicineData(){}

    // Fetches all medicines from the file
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header row
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 3) continue;

                try {
                    String name = data[0].trim();
                    int stock = Integer.parseInt(data[1].trim());
                    int lowLevel = Integer.parseInt(data[2].trim());
                    Medicine med = new Medicine(name, stock, lowLevel);
                    medicines.add(med);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading medicine data.");
            e.printStackTrace();
        }
        return medicines;
    }
    
    // Retrieves a single medicine by name case insens
    public Medicine getMedicineByName(String name) {
        List<Medicine> medicines = getAllMedicines();
        for (Medicine med : medicines) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                return med;
            }
        }
        return null;
    }

    // Adds a new medicine to the database
    public boolean addMedicine(Medicine med) {
        if (exists(med.getMedicineName())) {
            return false; // Medicine already exists
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(med.toCSV());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Failed to add medicine to database.");
            e.printStackTrace();
        }
        return false;
    }

    // Checks if a medicine exists case insens
    public boolean exists(String name) {
        List<Medicine> medicines = getAllMedicines();
        for (Medicine med : medicines) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // Updates the stock of an existing medicine
    public boolean updateStock(String name, int changeAmount) {
        List<Medicine> medicineList = getAllMedicines();
        boolean updated = false;
        
        for (Medicine med : medicineList) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                med.setInitialStock(med.getInitialStock() + changeAmount);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return updateAllStock(medicineList); // Write updated stock back to file
        }
        
        return false; // Medicine not found
    }

    // Writes the updated list of all medicines back to the file
    private boolean updateAllStock(List<Medicine> medicineList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            writer.newLine();
            for (Medicine medicine : medicineList) {
                writer.write(medicine.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Failed to update stock data.");
            e.printStackTrace();
        }
        return false;
    }

    // Checks if the stock of a given medicine is below its low stock alert level
    public boolean stockLow(Medicine med) {
        return med.getInitialStock() <= med.getLowStockLevelAlert();
    }
}
