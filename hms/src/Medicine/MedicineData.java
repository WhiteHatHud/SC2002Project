package Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MedicineData} class provides methods to manage the data of medicines
 * stored in a CSV file. It allows fetching, adding, updating, and removing medicine
 * records. It also provides functionality to check the stock levels of medicines.
 */
public class MedicineData {

    /** Path to the CSV file where medicine data is stored */
    private static final String FILE_PATH = "Medicine_List.csv";

    /**
     * Default constructor for {@code MedicineData}.
     */
    public MedicineData() {}

    /**
     * Fetches all medicines from the file.
     *
     * @return a list of all medicines
     */
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
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

    /**
     * Gets the number of medicines in the database.
     *
     * @return the number of medicines
     */
    public int getNumMedicines() {
        return getAllMedicines().size();
    }

    /**
     * Retrieves a single medicine by its name, case-insensitively.
     *
     * @param name the name of the medicine
     * @return the {@code Medicine} object if found, or {@code null} if not found
     */
    public Medicine getMedicineByName(String name) {
        List<Medicine> medicines = getAllMedicines();
        for (Medicine med : medicines) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                return med;
            }
        }
        return null;
    }

    /**
     * Adds a new medicine to the database.
     *
     * @param med the {@code Medicine} object to add
     * @return {@code true} if the medicine was added, {@code false} if it already exists
     */
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

    /**
     * Checks if a medicine exists in the database, case-insensitively.
     *
     * @param name the name of the medicine to check
     * @return {@code true} if the medicine exists, {@code false} otherwise
     */
    public boolean exists(String name) {
        List<Medicine> medicines = getAllMedicines();
        for (Medicine med : medicines) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the stock of an existing medicine.
     *
     * @param name         the name of the medicine
     * @param changeAmount the amount to change the stock by (positive to increase, negative to decrease)
     * @return {@code true} if the stock was updated, {@code false} if the medicine was not found
     */
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
            return updateAllStock(medicineList);
        }
        
        return false; // Medicine not found
    }

    /**
     * Writes the updated list of all medicines back to the file.
     *
     * @param medicineList the list of medicines to write
     * @return {@code true} if the file was successfully updated, {@code false} otherwise
     */
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

    /**
     * Checks if the stock of a given medicine is below its low stock alert level.
     *
     * @param med the {@code Medicine} object to check
     * @return {@code true} if the stock is low, {@code false} otherwise
     */
    public boolean stockLow(Medicine med) {
        return med.getInitialStock() <= med.getLowStockLevelAlert();
    }

    /**
     * Removes a medicine from the list by name, case-insensitively.
     *
     * @param name the name of the medicine to remove
     * @return {@code true} if the medicine was removed, {@code false} if it was not found
     */
    public boolean removeMedicineByName(String name) {
        List<Medicine> medicineList = getAllMedicines();
        boolean removed = false;

        // Find and remove the medicine
        for (Medicine med : medicineList) {
            if (med.getMedicineName().equalsIgnoreCase(name)) {
                medicineList.remove(med);
                removed = true;
                break;
            }
        }

        if (removed) {
            return updateAllStock(medicineList); 
        }

        return false;
    }

    /**
     * Sets a new low stock level alert for a medicine by name, case-insensitively.
     *
     * @param name            the name of the medicine
     * @param newLowStockLevel the new low stock level alert to set
     * @return {@code true} if the alert level was updated, {@code false} if the medicine was not found
     */
    public boolean setLowStockLevelAlert(String name, int newLowStockLevel) {
        List<String[]> medicinesData = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Case insensitive check for the medicine name
                if (data[0].trim().equalsIgnoreCase(name) && data.length >= 3) {
                    data[2] = String.valueOf(newLowStockLevel); // Update the low stock level alert 
                    updated = true;
                }
                medicinesData.add(data); // Add the row to the list
            }
        } catch (IOException e) {
            System.out.println("Error reading medicine data.");
            e.printStackTrace();
            return false; // Error in reading file
        }

        // Write the updated list back to the CSV file if the medicine was found
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String[] data : medicinesData) {
                    writer.write(String.join(",", data));
                    writer.newLine();
                }
            
                System.out.println("Medicine " + name + " low level stock has been updated to " + newLowStockLevel);
                return true; // Successfully updated and saved
            } catch (IOException e) {
                System.out.println("Failed to write updated data to CSV file.");
                e.printStackTrace();
            }
        }

        return false; // Medicine not found
    }
}
