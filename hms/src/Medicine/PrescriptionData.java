package Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionData {
    private static final String FILE_PATH = "././Prescriptions_List.csv";
    List<Prescription> prescriptionList;
    public List<Prescription> getAllPrescriptions(){
        prescriptionList = new ArrayList<>();
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
                    int prescriptionAmount = Integer.parseInt(data[4].trim());
                    boolean status = Boolean.parseBoolean(data[4]);
                    Prescription pres = new Prescription(data[0], data[1], data[2], data[3], prescriptionAmount, status);
                    prescriptionList.add(pres);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prescriptionList;
    }  
    public List<Prescription> getPatientPrescriptionList(String patientID){
        prescriptionList = getAllPrescriptions();
        prescriptionList.removeIf(n -> (!n.getPatientID().equals(patientID)));
        return prescriptionList;
    }
    public Prescription getPatientPrescription(String patientID, String medicineID){
        prescriptionList = getPatientPrescriptionList(patientID);
        for (Prescription pres : prescriptionList){
            if(pres.getMedicineName().equals(medicineID)) return pres;
        }
        return null;
    }

    // if patient has matching prescription already, just add amount
    public boolean addPrescription(Prescription prescription){
        if (exists(prescription.getPatientID(), prescription.getMedicineName())){
            return false;
        }
        //add to database
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(prescription.toCSV());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if adding fails
        return false;
    }
    // returns true if patient has matching medicine already prescribed
    public boolean exists(String patientID, String medicineName){
        prescriptionList = getPatientPrescriptionList(patientID);
        for (Prescription pres : prescriptionList){
            pres.medicineName.equals(medicineName);
            return true;
        }
        return false;
    }
    public boolean updateAllPrescriptions(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            writer.newLine();
            for (Prescription pres : prescriptionList) {
                writer.write(pres.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean increaseAmount(String patientID, String medicineID, int newAmount){
        prescriptionList = getAllPrescriptions();
        for (Prescription pres : prescriptionList){
            if(exists(pres.getPatientID(), pres.getMedicineName())){
                pres.setPrescriptionAmount(newAmount);
                return updateAllPrescriptions();
            }
        }
        return false;
    }

    public boolean deletePrescription(String patientID, String medicineID){
        prescriptionList = getAllPrescriptions();
        return prescriptionList.removeIf(n -> 
        (n.getPatientID().equals(patientID) && n.getMedicineName().equals(medicineID)) 
        && updateAllPrescriptions());
    }
}
