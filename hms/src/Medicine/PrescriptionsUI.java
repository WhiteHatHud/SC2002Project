package Medicine;

import Login.DisplayFormat;
import Utilities.UserInputHandler;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrescriptionsUI {
    private PrescriptionData prescriptionData = new PrescriptionData();
    private UserInputHandler inputHandler = new UserInputHandler();
    private DisplayFormat displayFormat = new DisplayFormat();

    // Enum to represent actions in the menu
    public enum Action {
        VIEW_ALL, VIEW_PATIENT, ADD, UPDATE_STATUS, DELETE
    }

    // Permission set to control accessible actions
    private Set<Action> allowedActions;

    // Constructor to set up allowed actions based on role
    public PrescriptionsUI(Set<Action> allowedActions) {
        this.allowedActions = allowedActions;
    }

    // Method to display the menu based on allowed actions
    public void displayPrescriptionsMenu() {
        boolean isRunning = true;
        while (isRunning) {
            DisplayFormat.clearScreen();
            displayFormat.divider();
            DisplayFormat.printCentered("Prescription Management System", 73);
            displayFormat.divider();

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
            displayFormat.divider();
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
        DisplayFormat.clearScreen();
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
        DisplayFormat.clearScreen();
        System.out.print("Enter Patient ID: ");
        String patientID = inputHandler.getNextLine();
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
        DisplayFormat.clearScreen();
        System.out.print("Enter Prescription ID: ");
        String prescriptionID = inputHandler.getNextLine();

        System.out.print("Enter Patient ID: ");
        String patientID = inputHandler.getNextLine();

        System.out.print("Enter Patient Name: ");
        String patientName = inputHandler.getNextLine();

        System.out.print("Enter Doctor ID: ");
        String doctorID = inputHandler.getNextLine();

        System.out.print("Enter Doctor Name: ");
        String doctorName = inputHandler.getNextLine();

        System.out.print("Enter Date Prescribed (yyyy-MM-dd): ");
        LocalDate datePrescribed = LocalDate.parse(inputHandler.getNextLine());

        Map<String, Integer> medications = new HashMap<>();
        while (true) {
            System.out.print("Enter Medicine Name (or type 'done' to finish): ");
            String medicineName = inputHandler.getNextLine();
            if ("done".equalsIgnoreCase(medicineName)) {
                break;
            }

            System.out.print("Enter Dosage (mg): ");
            int dosage = inputHandler.getUserChoice();
            medications.put(medicineName, dosage);
        }

        System.out.print("Enter Status: ");
        String status = inputHandler.getNextLine();

        System.out.print("Enter Notes: ");
        String notes = inputHandler.getNextLine();

        Prescription prescription = new Prescription(prescriptionID, patientID, patientName, doctorID, doctorName,
                                                     datePrescribed, medications, status, notes);
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
        DisplayFormat.clearScreen();
        System.out.print("Enter Prescription ID: ");
        String prescriptionID = inputHandler.getNextLine();

        System.out.print("Enter New Status: ");
        String newStatus = inputHandler.getNextLine();

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
        DisplayFormat.clearScreen();
        System.out.print("Enter Patient ID: ");
        String patientID = inputHandler.getNextLine();
        
        System.out.print("Enter Medicine Name: ");
        String medicineName = inputHandler.getNextLine();

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
        displayFormat.divider();
        DisplayFormat.printCentered("Prescription Details", 73);
        displayFormat.divider();
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
        displayFormat.divider();
    }
}
