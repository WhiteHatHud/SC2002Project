package Medicine;

import Login.DisplayManager;
import Utilities.UserInputHandler;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import Patients.PatientData;
import Users.StaffData;
import Pharmacists.*;

public class PrescriptionsUI {
    private PrescriptionData prescriptionData = new PrescriptionData();
    private UserInputHandler inputHandler = new UserInputHandler();
    private DisplayManager uiManager = new DisplayManager();
    private PatientData patientData = new PatientData();
    private StaffData staffData = new StaffData();
    private MedicineData medicineData = new MedicineData();
    private RequestFormController request;
    private String error = "";
    private Pharmacist pharma;

    // Enum to represent actions in the menu
    public enum Action {
        VIEW_ALL, VIEW_PATIENT, ADD, UPDATE_STATUS, DELETE
    }

    // Permission set to control accessible actions
    private Set<Action> allowedActions;

    // Constructor to set up allowed actions based on role
    public PrescriptionsUI(Set<Action> allowedActions, Pharmacist pharmacist) {
        this.allowedActions = allowedActions;
        this.pharma = pharmacist;
    }

    // Method to display the menu based on allowed actions
    public void displayPrescriptionsMenu() {
        boolean isRunning = true;
        while (isRunning) {
            DisplayManager.clearScreen();
            error = DisplayManager.loadErrorMessage(error);
            uiManager.divider();
            DisplayManager.printCentered("Prescription Management System", 73);
            uiManager.divider();

            int optionNumber = 1;
            if (allowedActions.contains(Action.VIEW_ALL)) {
                System.out.println(optionNumber++ + ". View All Prescriptions");
            }
            if (allowedActions.contains(Action.VIEW_PATIENT)) {
                System.out.println(optionNumber++ + ". View Prescriptions for a Patient");
            }
            if (allowedActions.contains(Action.ADD)) {
                System.out.println(optionNumber++ + ". Add New Prescription");
            }
            if (allowedActions.contains(Action.UPDATE_STATUS)) {
                System.out.println(optionNumber++ + ". Update Prescription Status");
            }
            if (allowedActions.contains(Action.DELETE)) {
                System.out.println(optionNumber++ + ". Delete Prescription");
            }
            System.out.println(optionNumber + ". Exit");
            uiManager.divider();
            System.out.print("Please select an option: ");

            int choice = inputHandler.getUserChoice();
            isRunning = handleChoice(choice, optionNumber);
        }
        System.out.println("Exiting Prescription Management System.");
    }

    // Handle menu choices based on allowed actions
    private boolean handleChoice(int choice, int exitOption) {
        int optionNumber = 1;

        if (allowedActions.contains(Action.VIEW_ALL) && choice == optionNumber++) {
            viewAllPrescriptions();
            return true;
        }
        if (allowedActions.contains(Action.VIEW_PATIENT) && choice == optionNumber++) {
            viewPatientPrescriptions();
            return true;
        }
        if (allowedActions.contains(Action.ADD) && choice == optionNumber++) {
            addNewPrescription();
            return true;
        }
        if (allowedActions.contains(Action.UPDATE_STATUS) && choice == optionNumber++) {
            updatePrescriptionStatus();
            return true;
        }
        if (allowedActions.contains(Action.DELETE) && choice == optionNumber++) {
            deletePrescription();
            return true;
        }
        // Exit
        if (choice == exitOption) {
            return false;
        }

        System.out.println("Invalid option. Please try again.");
        return true;
    }

    // Implement the original six functions

    private void viewAllPrescriptions() {
        DisplayManager.clearScreen();
        List<Prescription> prescriptions = prescriptionData.getAllPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found.");
        } else {
            for (Prescription pres : prescriptions) {
                printPrescription(pres);
            }
        }
        System.out.println("Press Enter to return to the main menu.");
        inputHandler.getNextLine();  // Wait for user input before returning
    }

    private void viewPatientPrescriptions() {
        DisplayManager.clearScreen();
        String patientID = validatePatientID();
        if (patientID == null) return;

        List<Prescription> prescriptions = prescriptionData.getPatientPrescriptionList(patientID);
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found for patient ID: " + patientID);
        } else {
            for (Prescription pres : prescriptions) {
                printPrescription(pres);
            }
        }
        System.out.println("Press Enter to return to the main menu.");
        inputHandler.getNextLine();  // Wait for user input before returning
    }

    private void addNewPrescription() {
        DisplayManager.clearScreen();
        System.out.print("Enter Prescription ID: ");
        String prescriptionID = inputHandler.getNextLine();
        //set patient detials
        String patientID = validatePatientID();
        if (patientID == null) return;
        String patientName = patientData.getPatientByID(patientID).getName();
        //set doctor details
        String doctorID = validateDoctorID();
        if (doctorID == null) return;
        String doctorName = staffData.getStaffByID(doctorID).getName();
        System.out.print("Enter Date Prescribed (yyyy-MM-dd): ");
        LocalDate datePrescribed = LocalDate.parse(inputHandler.getNextLine());
        Map<String, Integer> medications = new HashMap<>();
        while (true) {
            String medicineName = validateMedicineName();
            if (medicineName == null) {
                DisplayManager.printCentered(error, 80);
                error = "";
                continue;
            }
            if ("done".equalsIgnoreCase(medicineName)) {
                break;
            }
            System.out.print("Enter Dosage (mg): ");
            int dosage = inputHandler.getUserChoice();
            medications.put(medicineName, dosage);
        }
        System.out.print("Enter Notes: ");
        String notes = inputHandler.getNextLine();
        Prescription prescription = new Prescription(prescriptionID, patientID, patientName, doctorID, doctorName,
                                                     datePrescribed, medications, "pending", notes);
        boolean success = prescriptionData.addPrescription(prescription);
        if (success) {
            System.out.println("Prescription successfully added.");
        } else {
            System.out.println("Failed to add prescription. It may already exist.");
        }
        System.out.println("Press Enter to return to the main menu.");
        inputHandler.getNextLine();  // Wait for user input before returning
    }

    private void updatePrescriptionStatus() {
        int choice;
        DisplayManager.clearScreen();
        String prescriptionID = validatePrescriptionID();
        if (prescriptionID == null) return;
        Prescription pres = prescriptionData.getPrescription(prescriptionID);
        if (pres.getStatus().equals("Dispensed")) {
            error = prescriptionID+ " has already been dispensed";
            return;
        }
        System.out.println("Choose update Status: ");
        System.out.println("(1) Dispense");
        //System.out.println("(2) Dispensed");
        DisplayManager.printCentered("Enter choide: ", 80);
        if ((choice = inputHandler.getUserChoice(2)) == -1){
            error = "Invalid Input.";
            return; 
        } 
        String newStatus = "Pending";
        switch(choice){

            case 1:
                newStatus = "Dispensed";  
                Map<String, Integer> medications = pres.getMedications();
                for (Map.Entry<String, Integer> entry : medications.entrySet()) {
                    String medicineName = entry.getKey();
                    int dosage = entry.getValue();
                    if ((medicineData.getMedicineByName(medicineName).getInitialStock() - dosage) < 0){
                        error = "Not enough stock to dispense " + medicineName + ". Please submit to request update stock.";
                        return;
                    }
                    boolean stockUpdated = medicineData.updateStock(medicineName, -dosage);
                    Medicine med = medicineData.getMedicineByName(medicineName);
                    //System.out.println(med.getLowStockLevelAlert() + " " + med.getInitialStock());
                    if(med.getLowStockLevelAlert() >= med.getInitialStock()){
                        DisplayManager.printCentered(medicineName + " is low at: " + med.getInitialStock(), 80);
                        request = new RequestFormController(pharma);
                        error += " " + request.request(pharma.getName(), pharma.getUserID(), medicineName);
                    }
                    if (!stockUpdated) {
                        System.out.println("Failed to update stock for medicine: " + medicineName);
                    } else {
                        System.out.println("Stock updated successfully for medicine: " + medicineName);
                    }
                }
                break;
            }
        boolean success = prescriptionData.updatePrescriptionStatus(prescriptionID, newStatus);
        if (success) {
            System.out.println("Prescription status updated successfully.");
        } else {
            System.out.println("Failed to update prescription status.");
        }
        System.out.println("Press Enter to return to the main menu.");
        inputHandler.getNextLine();  // Wait for user input before returning
    }

    private void deletePrescription() {
        DisplayManager.clearScreen();
        String patientID = validatePatientID();
        if (patientID == null) return;
        String medicineName = validateMedicineName();
        if (medicineName == null) return;
        boolean success = prescriptionData.deletePrescription(patientID, medicineName);
        if (success) {
            System.out.println("Prescription successfully deleted.");
        } else {
            System.out.println("Failed to delete prescription.");
        }
        System.out.println("Press Enter to return to the main menu.");
        inputHandler.getNextLine();  // Wait for user input before returning
    }

    private void printPrescription(Prescription pres) {
        uiManager.divider();
        DisplayManager.printCentered("Prescription Details", 73);
        uiManager.divider();
        System.out.println("Prescription ID: " + pres.getPrescriptionID());
        System.out.println("Patient ID: " + pres.getPatientID());
        System.out.println("Patient Name: " + pres.getPatientName());
        System.out.println("Doctor ID: " + pres.getDoctorID());
        System.out.println("Doctor Name: " + pres.getDoctorName());
        System.out.println("Date Prescribed: " + pres.getDatePrescribed());

        System.out.println("Medications:");
        for (Map.Entry<String, Integer> entry : pres.getMedications().entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + "mg");
        }

        System.out.println("Status: " + pres.getStatus());
        System.out.println("Notes: " + pres.getNotes());
        uiManager.divider();
    }

    private String validatePrescriptionID() {
        System.out.print("Enter Prescription ID: ");
        String prescriptionID = inputHandler.getNextLine();
    
        if (!prescriptionData.checkPrescriptionExists(prescriptionID)) {
            error = "Invalid Prescription ID.";
            return null; // Return null to indicate invalid ID
        }
        return prescriptionID; // Return the valid ID
    }
    private String validatePatientID() {
        System.out.print("Enter patient ID: ");
        String patientID = inputHandler.getNextLine();
    
        if (!patientData.exists(patientID)) {
            error = "Invalid Patient ID.";
            return null; // Return null to indicate invalid ID
        }
        return patientID; // Return the valid ID
    }

    private String validateDoctorID() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = inputHandler.getNextLine();
    
        if (!staffData.exists(doctorID)) {
            error = "Invalid Doctor ID.";
            return null; // Return null to indicate invalid ID
        }
        return doctorID; // Return the valid ID
    }

    private String validateMedicineName() {
        System.out.print("Enter Medicine Name (or type 'done' to finish): ");
        String medicineName = inputHandler.getNextLine();
    
        if (!medicineData.exists(medicineName)) {
            error = "Invalid medicine name.";
            return null; // Return null to indicate invalid ID
        }
        return medicineName; // Return the valid ID
    }
    
}
