package Medicine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Login.DisplayManager;
import Pharmacists.Pharmacist;
import Users.StaffData;
import Utilities.CSVUpdater;
import Utilities.UserInputHandler;

public class RequestFormController extends DisplayManager{
    private RequestForm form;
    private StaffData staffData = new StaffData();
    private MedicineUI medicine = new MedicineUI();
    private MedicineData medicineData = new MedicineData();
    private DisplayManager display = new DisplayManager();
    private UserInputHandler input = new UserInputHandler();
    private static final String FILE_PATH = "RequestFromPharma.csv";
    private CSVUpdater csvUpdater = new CSVUpdater(FILE_PATH);
    private Pharmacist staff;
    public RequestFormController(Pharmacist staff){
        this.staff = staff;
    }

    public void approve(){
        DisplayManager.clearScreen();
        display.divider();
    } 
    public String request(){
        int index, amount;
        DisplayManager.clearScreen();
        display.divider();
        medicine.displayAllMedicines();
        DisplayManager.printCentered("Which Medicine would you like to request (Choose index).", 80);
        index = input.getPositiveInt(medicineData.getNumMedicines());
        DisplayManager.printCentered("Select amount to request", 80);
        for (int i = 100; i<=500; i += 100){
            System.out.println((i/100)+". "+i+"mg");
        }
        DisplayManager.printCentered("Enter Choice: ", 80);
        if ((amount = input.getUserChoice()) < 0 || amount > 5) return "Invalid Input. Please try again.";
        form = new RequestForm(generateRequestID(), medicineData.getAllMedicines().get(index-1).getMedicineName(), amount*100, staff.getUserID(), staff.getName());
        csvUpdater.addNewLineToCSV(form.toCSVArray(), FILE_PATH, 5);
        
        return "Succesfully Requested";
    }
    public String request(String requestorName, String requestorID, String medicineName){
        //DisplayManager.clearScreen();
        int amount = -1, i;
        display.divider();
        DisplayManager.printCentered("Request for Medicine re-stock of " + medicineName, 80);
        display.divider();
        DisplayManager.printCentered("Select amount to request (1=100mg)", 80);
        for (i = 100; i<=500; i += 100){
            System.out.println((i/100)+". "+i);
        }
        DisplayManager.printCentered("Enter Choice: ", 80);
        amount = input.getPositiveInt(i/100);
        form = new RequestForm(generateRequestID(), medicineName, amount*100, requestorID, requestorName);
        csvUpdater.addNewLineToCSV(form.toCSVArray(), FILE_PATH, 5);
        
        return "Succesfully Requested " + medicineName + "\n";
    }
    
    public void delete(){

    }
     public String generateRequestID() {
        String prefix = "RQ"; // Prefix for Prescription IDs
        int highestID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].startsWith(prefix)) { 
                    int idNumber = Integer.parseInt(fields[0].substring(2));
                    if (idNumber > highestID) {
                        highestID = idNumber;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int newIDNumber = highestID + 1;
        return prefix + String.format("%03d", newIDNumber); // Returns ID in the format PR001, PR002, etc.
    }
    
}
