package Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineData {
    private static final String FILE_PATH = "././Medicine_List.csv";
    Medicine temp;
    public MedicineData(){}

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine();
            // Skip header row
            while ((line = reader.readLine()) != null) {
                // System.out.println("Reading line: " + line); // Debugging print statement
                String[] data = line.split(",");
                if (data.length < 3) {
                    //System.out.println("Skipping incomplete line: " + line); // Debugging print for skipped lines
                    continue;
                }
                try {
                    String name = data[0].trim();
                    int stock = Integer.parseInt(data[1].trim());
                    int lowLevel = Integer.parseInt(data[2].trim());
                    Medicine med = new Medicine(name, stock, lowLevel);
                    medicines.add(med);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicines;
    }
    
    public Medicine getMedicineByName(String name){
        List<Medicine> medicines = getAllMedicines();
        for (Medicine med : medicines){
            //System.out.println(med.getMedicineName() + " " + name);
            if(med.getMedicineName().equals(name)){
                //System.out.println("Returning: " + med.medicineName);
                return med;
            }
        }
        return null;
    }

    public boolean addMedicine(Medicine med){
        //if already in database
        if (exists(med.medicineName)){
            return false;
        }
        //add to database
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(med.toCSV());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if adding fails
        return false;
    }

    public boolean exists(String name){
        if (getMedicineByName(name) != null){
            return true;
        }
        //System.out.println(name + " not found");
        return false;
    }

    public boolean updateStock(String name, int changeAmount){
        if(exists(name)){
            List <Medicine> medicineList = getAllMedicines();
            for (Medicine replace : medicineList){
                if (replace.medicineName.equals(name)){
                    replace.initialStock += changeAmount;
                }
            }
            return updateAllStock(medicineList);
        }
        return false;
    }

    public boolean updateAllStock(List <Medicine> medicineList){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            writer.newLine();
            for (Medicine medicine : medicineList) {
                writer.write(medicine.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean stockLow(Medicine med){
        if(med.getInitialStock() <= med.initialStock){
            return true;
        }
        return false;
    }
}
