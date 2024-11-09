package Pharmacists;

import Medicine.PrescriptionsUI;
import Medicine.RequestFormController;
import Medicine.PrescriptionsUI.Action;
import Utilities.LogoutTimer;
import Utilities.UserInputHandler;
import java.util.EnumSet;

import Login.DisplayManager;
import Medicine.MedicineUI;
import Patients.Patient;
import Patients.PatientData;
import Patients.ViewMedicalRecord;

public class PharmaController {
    private Pharmacist pharmacist;
    private PharmaDisplayManager displayManager;
    private PrescriptionsUI prescriptionsUI;
    private UserInputHandler inputHandler;
    private RequestData requestData;
    private MedicineUI medicineUI;
    private PatientData patientData;
    private String errorMessage;
    private RequestFormController form;

    public PharmaController(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
        this.displayManager = new PharmaDisplayManager();
        this.prescriptionsUI = new PrescriptionsUI(EnumSet.of(Action.VIEW_ALL, Action.VIEW_PATIENT, Action.UPDATE_STATUS), pharmacist);
        this.inputHandler = new UserInputHandler();
        this.requestData = new RequestData();
        this.medicineUI = new MedicineUI();
        this.errorMessage = "";
        this.patientData = new PatientData();
        this.form = new RequestFormController(pharmacist);
    }

    public void start() {
        boolean isActive = true;
        while (isActive) {
            DisplayManager.clearScreen();
            errorMessage = PharmaDisplayManager.loadErrorMessage(errorMessage);
            displayManager.displayMainMenu();  // Call display manager to show the main menu
            int choice = inputHandler.getUserChoice();
            isActive = handleMainChoice(choice);
        }
    }

    private boolean handleMainChoice(int choice) {
        PharmaDisplayManager.clearScreen();

        switch (choice) {
            case 1:
                viewAppointmentOutcomeRecord();
                break;
            case 2:
                prescriptionsUI.displayPrescriptionsMenu();
                break;
            case 3:
                medicineUI.displayAllMedicines();
                break;
            case 4:
                errorMessage = form.request();
                //inputHandler.getNextLine();
                break;

            case 5: // Logout

                if (LogoutTimer.confirmLogout()) {
                    return false; // Ends the session only if logout is confirmed
                } else {
                    PharmaDisplayManager.clearScreen();
                    return true; // Continue the session without printing additional messages
                }

            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return true;
    }

    private void viewAppointmentOutcomeRecord() {
        // appointmentoutcomerecord logic
        PharmaDisplayManager.printCentered("Enter the patient's ID", 80);
        String patientID = inputHandler.getNextLine();
        Patient patient = patientData.getPatientByID(patientID);
        if (patient == null){
            errorMessage = "Invalid PatientID";
            return;
        }
        ViewMedicalRecord view = new ViewMedicalRecord();
        view.viewMedicalRecord(patient);
    }
}
